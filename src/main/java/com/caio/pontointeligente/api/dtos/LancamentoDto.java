package com.caio.pontointeligente.api.dtos;

import java.util.Optional;

import javax.validation.constraints.NotNull;

public class LancamentoDto {
	
	private Optional<Long> id = Optional.empty();
	private String data;
	private String tipo;
	private String descricao;
	private String localizacao;
	private Long funcionarioId;
	
	public Optional<Long> getId() {
		return id;
	}
	
	public void setId(Optional<Long> id) {
		this.id = id;
	}
	
	@NotNull(message = "A data é obrigatória")
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getLocalizacao() {
		return localizacao;
	}
	
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	public Long getFuncionarioId() {
		return funcionarioId;
	}
	
	public void setFuncionarioId(Long funcionarioId) {
		this.funcionarioId = funcionarioId;
	}
	
}
