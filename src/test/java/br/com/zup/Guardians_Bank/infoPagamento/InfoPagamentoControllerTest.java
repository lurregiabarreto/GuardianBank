package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.components.Conversor;
import br.com.zup.Guardians_Bank.infoPagamento.dto.EntradaInfoDTO;
import br.com.zup.Guardians_Bank.infoPagamento.dto.SaidaInfoDTO;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import br.com.zup.Guardians_Bank.proposta.PropostaService;
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

@WebMvcTest({InfoPagamentoController.class, Conversor.class})
public class InfoPagamentoControllerTest {

  @MockBean
  private InfoPagamentoService infoPagamentoService;
  @MockBean
  private PropostaService propostaService;

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private InfoPagamento infoPagamento;
  private EntradaInfoDTO entradaInfoDTO;
  private SaidaInfoDTO saidaInfoDTO;
  private Proposta proposta;


  @BeforeEach
  public void setup() {
    objectMapper = new ObjectMapper();
    proposta = new Proposta();

    infoPagamento = new InfoPagamento();
    infoPagamento.setIdPagamento("1");
    infoPagamento.setProposta(proposta);
    infoPagamento.setValorParcela(1200.00);
    infoPagamento.setQtdadeDeParcelas(4);
    infoPagamento.setImposto(0.25);

    entradaInfoDTO = new EntradaInfoDTO();
    entradaInfoDTO.setNumeroProposta("1");
    entradaInfoDTO.setQtdadeParcelas(8);

    saidaInfoDTO = new SaidaInfoDTO();

  }

  @Test
  public void testarCadastroInfoPagamento() throws Exception {
    Mockito.when(infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt())).thenReturn(infoPagamento);

    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect((MockMvcResultMatchers.status().is(201)));

    String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    SaidaInfoDTO respostaInfo = objectMapper.readValue(jsonResposta, SaidaInfoDTO.class);
  }

  @Test
  public void testarValidacaoNumeroProposta() throws Exception {
    entradaInfoDTO.setNumeroProposta("    ");
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

  @Test
  public void testarValidacao() throws Exception {
    entradaInfoDTO.setQtdadeParcelas(null);
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

}