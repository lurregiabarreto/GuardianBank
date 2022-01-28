package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.components.Conversor;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoController;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoService;
import br.com.zup.Guardians_Bank.infoPagamento.dto.RespostaAtualizacaoStatusDTO;
import br.com.zup.Guardians_Bank.infoPagamento.dto.ResumoInfoDTO;
import br.com.zup.Guardians_Bank.infoPagamento.dto.RetornoInfoDTO;
import br.com.zup.Guardians_Bank.proposta.dtos.OpcoesPagamentoDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest({PropostaController.class, Conversor.class})
public class PropostaControllerTest {

  @MockBean
  private PropostaService propostaService;
  @MockBean
  private InfoPagamentoService infoPagamentoService;

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private Proposta proposta;
  private OpcoesPagamentoDTO opcoesPagamentoDTO;
  private RetornoInfoDTO retornoInfoDTO;

  @BeforeEach
  public void setup() {
    objectMapper = new ObjectMapper();

    proposta = new Proposta();
    proposta.setNumeroProposta("2");
    proposta.setProdutoFinanceiro(ProdutoFinanceiro.CONSIGNADO);
    proposta.setValorProposta(1200.00);
    proposta.setStatusProposta(StatusProposta.APROVADO);

    opcoesPagamentoDTO = new OpcoesPagamentoDTO();
    retornoInfoDTO = new RetornoInfoDTO();
    List<RetornoInfoDTO> list = new ArrayList<>();
    opcoesPagamentoDTO.setOpcoes(list);
    retornoInfoDTO.setQtdadeParcelas(2);
    retornoInfoDTO.setValorParcela(800);

  }

  @Test
  public void testarExibirOpcoesPagamento() throws Exception {
    Mockito.when(propostaService.exibirOpcoesValidadas(Mockito.anyString())).thenReturn(opcoesPagamentoDTO);
    String json = objectMapper.writeValueAsString(opcoesPagamentoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.get("/propostas/1")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(200));

    String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    OpcoesPagamentoDTO respostaAtualizada = objectMapper.readValue(jsonResposta,
        OpcoesPagamentoDTO.class);
  }

}
