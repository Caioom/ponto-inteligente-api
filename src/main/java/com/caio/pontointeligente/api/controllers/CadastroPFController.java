package com.caio.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caio.pontointeligente.api.dtos.CadastroPFDto;
import com.caio.pontointeligente.api.enums.PerfilEnum;
import com.caio.pontointeligente.api.models.Empresa;
import com.caio.pontointeligente.api.models.Funcionario;
import com.caio.pontointeligente.api.response.Response;
import com.caio.pontointeligente.api.services.EmpresaService;
import com.caio.pontointeligente.api.services.FuncionarioService;
import com.caio.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin
public class CadastroPFController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto, BindingResult result) {
		log.info("Cadastrando PF: {}", cadastroPFDto.toString());
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();
		
		validaDadosExistentes(cadastroPFDto, result);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDto);
		
		if(result.hasErrors()) {
			log.error("Erro no cadastro da PF: {}", result.getAllErrors());
			ArrayList<String> erros = new ArrayList<String>();
			result.getAllErrors().forEach(error -> erros.add(error.getDefaultMessage()));
			response.setErrors(erros);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterFuncionarioParaDto(funcionario));
		return ResponseEntity.ok(response);
 	}
	
	/**
	 * Converte DTO para funcionário afim de trabalhar sob os dados 
	 * 
	 * @param cadastroPFDto
	 * @return Funcionario
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPFDto cadastroPFDto) {
		Funcionario funcionario = new Funcionario();
		
		funcionario.setNome(cadastroPFDto.getNome());
		funcionario.setCpf(cadastroPFDto.getCpf());
		funcionario.setEmail(cadastroPFDto.getEmail());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));
		cadastroPFDto.getQtdHorasAlmoco()
			.ifPresent(hrsAlmoco -> funcionario.setQtdHorasAlmoco(Float.parseFloat(hrsAlmoco)));
		cadastroPFDto.getQtdHorasTrabalhoDia()
			.ifPresent(hrsTrabalho -> funcionario.setQtdHorasTrabalhoDia(Float.parseFloat(hrsTrabalho)));
		cadastroPFDto.getValorHora().ifPresent(valor -> funcionario.setValorHora(new BigDecimal(valor)));
		
		return funcionario;
	}
	
	/**
	 * Valida se alguns dados passados já existem
	 * 
	 * @param cadastroPFDto
	 * @param result
	 */
	public void validaDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
		if(!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Esta empresa não existe"));
		}
		
		this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Este CPF já tem cadastro")));
		
		this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Este email já existe")));
	}
	
	/**
	 * Converte os dados depois de trabalhados para o DTO de resposta 
	 * 
	 * @param funcionario
	 * @return CadastroPFDto
	 */
	public CadastroPFDto converterFuncionarioParaDto(Funcionario funcionario) {
		CadastroPFDto cadastro = new CadastroPFDto();
		
		cadastro.setId(funcionario.getId());
		cadastro.setNome(funcionario.getNome());
		cadastro.setEmail(funcionario.getEmail());
		cadastro.setCpf(funcionario.getCpf());
		cadastro.setCnpj(funcionario.getEmpresa().getCnpj());
		
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(hrsTrabalho -> cadastro
					.setQtdHorasTrabalhoDia(Optional.of(hrsTrabalho.toString())));
		
		funcionario.getQtdHorasAlmocoOpt().ifPresent(hrsAlmoco -> cadastro
					.setQtdHorasAlmoco(Optional.of(hrsAlmoco.toString())));
		
		funcionario.getValorHoraOpt().ifPresent(valor -> cadastro
					.setValorHora(Optional.of(valor.toString())));
		
		return cadastro;
	}
}
