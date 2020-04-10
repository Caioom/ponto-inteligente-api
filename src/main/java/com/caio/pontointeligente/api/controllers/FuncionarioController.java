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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caio.pontointeligente.api.dtos.FuncionarioDto;
import com.caio.pontointeligente.api.models.Funcionario;
import com.caio.pontointeligente.api.response.Response;
import com.caio.pontointeligente.api.services.FuncionarioService;
import com.caio.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin
public class FuncionarioController {
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);
	ArrayList<String> erros = new ArrayList<String>();
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<FuncionarioDto>> atualizar(@Valid @PathVariable("id") Long id, 
				@RequestBody FuncionarioDto funcionarioDto, BindingResult result) {
		log.info("Atualizando funcionário de id {}", id);
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);
		if(!funcionario.isPresent()) {
			log.error("Não foi possível encontrar o funcionário de id " + id);
			erros.add("Não foi possível encontrar o id " + id);
			
			response.setErrors(erros);
			return ResponseEntity.badRequest().body(response);
		}
		
		this.atualizarDadosFuncionario(funcionario.get(), funcionarioDto, result);
		if(result.hasErrors()) {
			result.getAllErrors().forEach(erro -> erros.add(erro.getDefaultMessage()));
			
			response.setErrors(erros);
			return ResponseEntity.badRequest().body(response);
		}
		
		this.funcionarioService.persistir(funcionario.get());
		response.setData(this.converterFuncionarioDto(funcionario.get()));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Converte um funcionário para um DTO response
	 * 
	 * @param funcionario
	 * @return FuncionarioDto
	 */
	private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {
		FuncionarioDto funcionarioDto = new FuncionarioDto();
		funcionarioDto.setId(funcionario.getId());
		funcionarioDto.setNome(funcionario.getNome());
		funcionarioDto.setEmail(funcionario.getEmail());
		
		funcionario.getQtdHorasAlmocoOpt()
			.ifPresent(hrsAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(hrsAlmoco.toString())));
		
		funcionario.getQtdHorasTrabalhoDiaOpt()
			.ifPresent(hrsTrabalho -> funcionario.setQtdHorasTrabalhoDia(hrsTrabalho));
		
		funcionario.getValorHoraOpt().ifPresent(valor -> funcionario.setValorHora(valor));
		
		return funcionarioDto;
	}
	
	/**
	 * Trabalha sob os dados recebidos no DTO de request e os armazena no objeto funcionario
	 * 
	 * @param funcionario
	 * @param funcionarioDto
	 * @param result
	 */
	private void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult result) {
		funcionario.setNome(funcionarioDto.getNome());
		
		if(!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
			this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
				.ifPresent(erro -> result.addError(new ObjectError("funcionario", "Funcionário já existente")));
			funcionario.setEmail(funcionarioDto.getEmail());
		}
		
		funcionario.setQtdHorasAlmoco(null);
		funcionarioDto.getQtdHorasAlmoco()
			.ifPresent(hrsAlmoco -> funcionario.setQtdHorasAlmoco(Float.parseFloat(hrsAlmoco)));
		
		funcionario.setQtdHorasTrabalhoDia(null);
		funcionarioDto.getQtdHorasTrabalhoDia()
			.ifPresent(hrsTrabalho -> funcionario.setQtdHorasTrabalhoDia(Float.parseFloat(hrsTrabalho)));
		
		funcionario.setValorHora(null);
		funcionarioDto.getValorHora().ifPresent(valor -> funcionario.setValorHora(new BigDecimal(valor)));
		
		if(funcionarioDto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha().get()));
		}
	}
	
}
