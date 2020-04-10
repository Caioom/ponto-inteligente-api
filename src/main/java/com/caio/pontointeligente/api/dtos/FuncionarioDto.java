package com.caio.pontointeligente.api.dtos;

import java.util.Optional;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FuncionarioDto {
	
	private Long id;
	private String nome;
	private String email;
	private Optional<String> senha = Optional.empty();
	private Optional<String> valorHora = Optional.empty();
	private Optional<String> qtdHorasTrabalhoDia = Optional.empty();
	private Optional<String> qtdHorasAlmoco = Optional.empty();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@NotNull(message = "O nome não pode estar vazio")
	@Size(min = 3, max = 200, message = "O nome deve ter entre 3 e 200 caracteres")
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@NotNull(message = "O email não pode estar vazio")
	@Size(min = 5, max = 200, message = "O email deve ter entre 5 e 200 caracteres")
	@Email(message = "Email inválido")
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Optional<String> getSenha() {
		return senha;
	}
	
	public void setSenha(Optional<String> senha) {
		this.senha = senha;
	}
	
	public Optional<String> getValorHora() {
		return valorHora;
	}
	
	public void setValorHora(Optional<String> valorHora) {
		this.valorHora = valorHora;
	}
	
	public Optional<String> getQtdHorasTrabalhoDia() {
		return qtdHorasTrabalhoDia;
	}
	
	public void setQtdHorasTrabalhoDia(Optional<String> qtdHorasTrabalhoDia) {
		this.qtdHorasTrabalhoDia = qtdHorasTrabalhoDia;
	}
	
	public Optional<String> getQtdHorasAlmoco() {
		return qtdHorasAlmoco;
	}
	
	public void setQtdHorasAlmoco(Optional<String> qtdHorasAlmoco) {
		this.qtdHorasAlmoco = qtdHorasAlmoco;
	}
}
