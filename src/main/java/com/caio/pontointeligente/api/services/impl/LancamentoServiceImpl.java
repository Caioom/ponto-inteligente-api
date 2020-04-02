package com.caio.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.caio.pontointeligente.api.models.Lancamento;
import com.caio.pontointeligente.api.repositories.LancamentoRepository;
import com.caio.pontointeligente.api.services.LancamentoService;

@Service 
public class LancamentoServiceImpl implements LancamentoService {

	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	public Page<Lancamento> buscarFuncionarioPorId(Long funcionarioId, PageRequest pageRequest) {
		log.info("Buscando lancamentos para o funcionario de id {}", funcionarioId);
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long lancamentoId) {
		log.info("Buscando lancamento de id {}", lancamentoId);
		return lancamentoRepository.findById(lancamentoId);
	}

	@Override
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Persistindo lancamento no banco de dados");
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo lancamento de id {}", id);
		this.lancamentoRepository.deleteById(id);
	}

}
