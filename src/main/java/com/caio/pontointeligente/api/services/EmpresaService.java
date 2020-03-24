package com.caio.pontointeligente.api.services;

import java.util.Optional;

import com.caio.pontointeligente.api.models.Empresa;

public interface EmpresaService {
	
	/**
	 * Retorna a Empresa dado um CNPJ
	 * 
	 * @param CNPJ
	 * @return Optional<Empresa>
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	/**
	 * Persiste uma empresa 
	 * 
	 * @param empresa
	 * @return Empresa
	 */
	Empresa persistir(Empresa empresa);

}
