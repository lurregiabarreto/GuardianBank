package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.components.Conversor;
import br.com.zup.Guardians_Bank.config.JWT.JWTComponent;
import br.com.zup.Guardians_Bank.config.Security.UsuarioLoginService;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.enums.TipoDeParcela;
import br.com.zup.Guardians_Bank.exceptions.*;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamento;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoService;
import br.com.zup.Guardians_Bank.infoPagamento.dto.RetornoInfoDTO;
import br.com.zup.Guardians_Bank.proposta.dtos.OpcoesPagamentoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@WebMvcTest({PropostaController.class, Conversor.class, UsuarioLoginService.class, JWTComponent.class})
public class PropostaControllerTest {

    @MockBean
    private PropostaService propostaService;
    @MockBean
    private InfoPagamentoService infoPagamentoService;
    @MockBean
    private UsuarioLoginService usuarioLoginService;
    @MockBean
    private JWTComponent jwtComponent;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Proposta proposta;
    private RetornoInfoDTO retornoInfoDTO;
    private InfoPagamento infoPagamento;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        infoPagamento = new InfoPagamento();
        infoPagamento.setIdPagamento("1");
        infoPagamento.setImposto(0.05);
        infoPagamento.setValorParcela(600);
        infoPagamento.setQtdadeDeParcelas(4);
        infoPagamento.setTipoDeParcela(TipoDeParcela.REGULAR);

        proposta = new Proposta();
        proposta.setNumeroProposta("2");
        proposta.setProdutoFinanceiro(ProdutoFinanceiro.CONSIGNADO);
        proposta.setValorProposta(1200.00);
        proposta.setStatusProposta(StatusProposta.APROVADO);

        retornoInfoDTO = new RetornoInfoDTO();
        retornoInfoDTO.setQtdadeParcelas(2);
        retornoInfoDTO.setValorParcela(800);

    }

    /*@Test
    @WithMockUser("user@user.com")
    public void testarExibirOpcoesPagamento() throws Exception {
        Mockito.when(propostaService.atribuirPropostaNoInfoPagamento(Mockito.anyString())).thenReturn(infoPagamento);
        Mockito.when(propostaService.exibirListaPagamento(infoPagamento)).thenReturn(Arrays.asList(infoPagamento));
        //Mockito.when(infoPagamentoService.opcoesParcelamento(infoPagamento)).thenReturn(Arrays.asList(infoPagamento));

        String json = objectMapper.writeValueAsString(retornoInfoDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.get("/propostas/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        String jsonResposta = resultado.andReturn().getResponse().getContentAsString();

        List<RetornoInfoDTO> respostaAtualizada = objectMapper.readValue(jsonResposta, ArrayList.class);
        OpcoesPagamentoDTO opcoesPagamentoDTO = objectMapper.readValue(jsonResposta, OpcoesPagamentoDTO.class);
    }*/

    @Test
    @WithMockUser("user@user.com")
    public void testarDataInvalidaException() throws Exception {
        Mockito.doThrow(DataInvalidaException.class).when(propostaService).atribuirPropostaNoInfoPagamento(
                Mockito.anyString());

        String json = objectMapper.writeValueAsString(retornoInfoDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders
                        .get("/propostas/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(422));
    }

    @Test
    @WithMockUser("user@user.com")
    public void testarEmAnaliseException() throws Exception {
        Mockito.doThrow(EmAnaliseException.class).when(propostaService).atribuirPropostaNoInfoPagamento
                (Mockito.anyString());

        String json = objectMapper.writeValueAsString(retornoInfoDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders
                        .get("/propostas/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(422));
    }

    @Test
    @WithMockUser("user@user.com")
    public void testarPropostaNaoEncontradaException() throws Exception {
        Mockito.doThrow(PropostaNaoEncontradaException.class).when(propostaService)
                .atribuirPropostaNoInfoPagamento(Mockito.anyString());

        String json = objectMapper.writeValueAsString(retornoInfoDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders
                        .get("/propostas/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser("user@user.com")
    public void testarPropostaRecusadaException() throws Exception {
        Mockito.doThrow(PropostaRecusadaException.class).when(propostaService)
                .atribuirPropostaNoInfoPagamento(Mockito.anyString());

        String json = objectMapper.writeValueAsString(retornoInfoDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders
                        .get("/propostas/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(422));
    }


}
