package com.caio.pontointeligente.api.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.caio.pontointeligente.api.enums.PerfilEnum;
import com.caio.pontointeligente.api.enums.TipoEnum;
import com.caio.pontointeligente.api.models.Empresa;
import com.caio.pontointeligente.api.models.Funcionario;
import com.caio.pontointeligente.api.models.Lancamento;
import com.caio.pontointeligente.api.utils.PasswordUtils;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class LancamentoRepositoryTest {
	
	@Autowired
	private LancamentoRepository repositoryLancamento;
	
	@Autowired
	private EmpresaRepository repositoryEmpresa;
	
	@Autowired
	private FuncionarioRepository repositoryFuncionario;
	
	private Long funcionarioId;
	
	@BeforeAll
	public void setUp() throws NoSuchAlgorithmException {
		Empresa empresa = this.repositoryEmpresa.save(obterDadosEmpresa());
		
		Funcionario funcionario = this.repositoryFuncionario.save(obterDadosFuncionario(empresa));
		this.funcionarioId = funcionario.getId();
		
		this.repositoryLancamento.save(obterDadosLancamento(funcionario));
		this.repositoryLancamento.save(obterDadosLancamento(funcionario));
	}
	
	@AfterAll
	public void tearDown() {
		this.repositoryLancamento.deleteAll();
	}
	
	@Test
	public void testBuscarLancamentoPorFuncionarioId() {
		List<Lancamento> lancamento = this.repositoryLancamento.findByFuncionarioId(funcionarioId);
		
		assertEquals(2, lancamento.size());
	}
	
	@Test
	public void testBuscarLancamentoPorFuncionarioPaginado() {
		PageRequest page = PageRequest.of(0, 10);
		Page<Lancamento> lancamentos = this.repositoryLancamento.findByFuncionarioId(funcionarioId, page);
		
		assertEquals(2, lancamentos.getTotalElements());
	}
		
	private Lancamento obterDadosLancamento(Funcionario funcionario) {
		Lancamento lancamento = new Lancamento();
		lancamento.setFuncionario(funcionario);
		lancamento.setData(new Date());
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		lancamento.setDescricao("Lancamento test");
		lancamento.setLocalizacao("hortolandia");
		
		return lancamento;
	}
	
	private Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Fulano de Tal");
		funcionario.setQtdHorasAlmoco((float) 24.0);
		funcionario.setQtdHorasTrabalhoDia((float) 20);
		funcionario.setValorHora(new BigDecimal(100));
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf("45872221860");
		funcionario.setEmail("caio.ds.2003@gmail.com");
		funcionario.setEmpresa(empresa);
		
		return funcionario;
	}
	
	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa ambiental");
		empresa.setCnpj("123456");
		
		return empresa;
	}
}
