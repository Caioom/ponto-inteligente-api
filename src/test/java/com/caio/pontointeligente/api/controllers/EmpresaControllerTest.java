package com.caio.pontointeligente.api.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.caio.pontointeligente.api.models.Empresa;
import com.caio.pontointeligente.api.services.EmpresaService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmpresaControllerTest {
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmpresaService empresaService;
	
	private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/empresas/cnpj/";
	private static final Long ID = Long.valueOf(1);
	private static final String CNPJ = "55843572005";
	private static final String RAZAO_SOCIAL = "Caio it";
	
	@Test
	public void testBuscarPorCnpjErrado() throws Exception {
		BDDMockito.given(empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.empty());
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors").value("Não foi possível encontrar o cnpj " + CNPJ));
	}
	
	public void testBuscarEmpresa() throws Exception {
		BDDMockito.given(empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.of(this.obterEmpresa()));
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(ID))
			.andExpect(jsonPath("$.data.razaoSocial", equalTo(RAZAO_SOCIAL)))
			.andExpect(jsonPath("$.data.cnpj", equalTo(CNPJ)))
			.andExpect(jsonPath("$.data.errors").isEmpty());
	}
	
	private Empresa obterEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setId(ID);
		empresa.setRazaoSocial(RAZAO_SOCIAL);
		empresa.setCnpj(CNPJ);
		
		return empresa;
	}
}
