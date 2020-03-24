package com.caio.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caio.pontointeligente.api.models.Empresa;
import com.caio.pontointeligente.api.repositories.EmpresaRepository;
import com.caio.pontointeligente.api.services.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService{
	
	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	private EmpresaRepository empresaRepository;

	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("Buscando pelo CPNJ {}", cnpj);
		return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("Persistindo empresa no banco de dados");
		return empresaRepository.save(empresa);
	}

}
