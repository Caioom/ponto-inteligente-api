package com.caio.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.caio.pontointeligente.api.models.Lancamento;

public interface LancamentoService {
	
	/**
	 * Retorna uma lista páginada de determinado funcionário
	 * 
	 * @param funcionarioId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	Page<Lancamento> buscarFuncionarioPorId(Long funcionarioId, PageRequest pageRequest);
	
	/**
	 * Retorna um lancamento por id
	 * 
	 * @param lancamentoId
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> buscarPorId(Long lancamentoId);
	
	/**
	 * Persiste um lancamento
	 * 
	 * @param lancamento
	 * @return Lancamento
	 */
	Lancamento persistir(Lancamento lancamento);
	
	/**
	 * Remove um lancamento de acordo com o id
	 * 
	 * @param id
	 */
	void remover(Long id);
	
}
