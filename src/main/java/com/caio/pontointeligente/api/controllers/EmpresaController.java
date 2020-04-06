package com.caio.pontointeligente.api.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caio.pontointeligente.api.dtos.EmpresaDto;
import com.caio.pontointeligente.api.models.Empresa;
import com.caio.pontointeligente.api.response.Response;
import com.caio.pontointeligente.api.services.EmpresaService;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin
public class EmpresaController {
	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	/**
	 * Busca empresa por cnpj
	 * 
	 * @param cnpj
	 * @return ResponseEntity<Response<EmpresaDto>>
	 */
	@GetMapping("/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(@PathVariable("cnpj") String cnpj) {
		log.info("Buscando a empresa {}", cnpj);
		Response<EmpresaDto> response = new Response<EmpresaDto>();
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);
		
		if(!empresa.isPresent()) {
			log.error("Não foi encontrada a empresa {}", cnpj);
			ArrayList<String> erro = new ArrayList<String>();
			erro.add("Não foi possível encontrar o cnpj " + cnpj);
			
			response.setErrors(erro);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.convertendoParaDto(empresa.get()));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Transforma a empresa encontrada em um DTO de resposta 
	 * 
	 * @param empresa
	 * @return EmpresaDto
	 */
	private EmpresaDto convertendoParaDto(Empresa empresa) {
		EmpresaDto empresaDto = new EmpresaDto();
		empresaDto.setId(empresa.getId());
		empresaDto.setRazaoSocial(empresa.getRazaoSocial());
		empresaDto.setCnpj(empresa.getCnpj());
		
		return empresaDto;
	}
}
