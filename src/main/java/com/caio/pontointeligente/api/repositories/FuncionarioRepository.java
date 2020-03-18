package com.caio.pontointeligente.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.caio.pontointeligente.api.models.Funcionario;

@Transactional(readOnly = true)
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	
	Funcionario findByEmail(String email);
	
	Funcionario findByCpf(String cpf);
	
	Funcionario findByCpfOrEmail(String cpf, String email);
}
