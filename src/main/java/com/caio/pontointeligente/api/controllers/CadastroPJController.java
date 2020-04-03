package com.caio.pontointeligente.api.controllers;

import java.util.ArrayList;

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

import com.caio.pontointeligente.api.dtos.CadastroPJDto;
import com.caio.pontointeligente.api.enums.PerfilEnum;
import com.caio.pontointeligente.api.models.Empresa;
import com.caio.pontointeligente.api.models.Funcionario;
import com.caio.pontointeligente.api.response.Response;
import com.caio.pontointeligente.api.services.EmpresaService;
import com.caio.pontointeligente.api.services.FuncionarioService;
import com.caio.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin
public class CadastroPJController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);
	
	@Autowired
	private EmpresaService empresaService; 
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@PostMapping
	public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto, BindingResult result) {
		
		log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();
		
		this.validarDadosExistentes(cadastroPJDto, result);
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto);
		
		if(result.hasErrors()) {
			log.error("Erro nos dados do cadastro PJ: {}", result.getAllErrors());
			ArrayList<String> erros = new ArrayList<String>();
			result.getAllErrors().forEach(error -> erros.add(error.getDefaultMessage()));
			response.setErrors(erros);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroPJDto(funcionario));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Converte o valor obtido do cadastro DTO e cria um funcionario
	 * 
	 * @param cadastroPJDto
	 * @return Funcionario
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPJDto cadastroPJDto) {
		Funcionario funcionario = new Funcionario();
		
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));
		
		return funcionario;
	}
	
	/**
	 * Converte o DTO para uma empresa 
	 * 
	 * @param cadastroPJDto
	 * @return Empresa
	 */
	private Empresa converterDtoParaEmpresa(CadastroPJDto cadastroPJDto) {
		Empresa empresa = new Empresa();
		
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());
		
		return empresa;
	}
	
	/**
	 * Converte um funcionario para DTO novamente, afim de enviar a response ao client
	 * 
	 * @param funcionario
	 * @return CadastroPJDto
	 */
	private CadastroPJDto converterCadastroPJDto(Funcionario funcionario) {
		CadastroPJDto cadastro = new CadastroPJDto();
		cadastro.setId(funcionario.getId());
		cadastro.setNome(funcionario.getNome());
		cadastro.setEmail(funcionario.getEmail());
		cadastro.setCpf(funcionario.getCpf());
		cadastro.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastro.setCnpj(funcionario.getEmpresa().getCnpj());
		
		return cadastro;
	}
	
	/**
	 * Valida se existem funcionarios com os campos identicos 
	 * 
	 * @param cadastro
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPJDto cadastro, BindingResult result) {
		this.empresaService.buscarPorCnpj(cadastro.getCnpj())
			.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente")));
		
		this.funcionarioService.buscarPorCpf(cadastro.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Este cpf já existe")));
		
		this.funcionarioService.buscarPorEmail(cadastro.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Este email já existe")));
	}
}
