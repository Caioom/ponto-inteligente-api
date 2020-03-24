package com.caio.pontointeligente.api.services;

import java.util.Optional;

import com.caio.pontointeligente.api.models.Funcionario;

public interface FuncionarioService {
	
	/**
	 * Adiciona um funcionário a base de dados 
	 * 
	 * @param funcionario
	 * @return Funcionario
	 */
	Funcionario persistir(Funcionario funcionario);

	/**
	 * Busca um funcionário por cpf
	 * 
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	/**
	 * Retorna um funcionário por email
	 * 
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
	/**
	 * Retorna um funcionário por id
	 * 
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorId(Long id);
	
}
