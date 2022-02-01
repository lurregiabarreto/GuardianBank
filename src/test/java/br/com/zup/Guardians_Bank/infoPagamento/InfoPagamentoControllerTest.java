package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.components.Conversor;
import br.com.zup.Guardians_Bank.config.JWT.JWTComponent;
import br.com.zup.Guardians_Bank.config.Security.UsuarioLoginService;
import br.com.zup.Guardians_Bank.exceptions.LimiteExcedidoException;
import br.com.zup.Guardians_Bank.exceptions.PropostaJaCadastradaException;
import br.com.zup.Guardians_Bank.exceptions.PropostaNaoLiberadaException;
import br.com.zup.Guardians_Bank.infoPagamento.dto.*;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import br.com.zup.Guardians_Bank.proposta.PropostaService;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.Arrays;
import java.util.List;

@WebMvcTest({InfoPagamentoController.class, Conversor.class, UsuarioLoginService.class, JWTComponent.class})
public class InfoPagamentoControllerTest {

  @MockBean
  private InfoPagamentoService infoPagamentoService;
  @MockBean
  private PropostaService propostaService;
  @MockBean
  private UsuarioLoginService usuarioLoginService;
  @MockBean
  private JWTComponent jwtComponent;

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private InfoPagamento infoPagamento;
  private EntradaInfoDTO entradaInfoDTO;
  private SaidaInfoDTO saidaInfoDTO;
  private AtualizarStatusDTO atualizarStatusDTO;
  private RespostaAtualizacaoStatusDTO respostaAtualizacaoStatusDTO;
  private ResumoInfoDTO resumoInfoDTO;
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
    atualizarStatusDTO = new AtualizarStatusDTO();
    respostaAtualizacaoStatusDTO = new RespostaAtualizacaoStatusDTO();
    resumoInfoDTO = new ResumoInfoDTO();

  }

  @Test
  @WithMockUser("user@user.com")
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
  @WithMockUser("user@user.com")
  public void testarCadastrarComValidacaoNumeroPropostaBlank() throws Exception {
    entradaInfoDTO.setNumeroProposta("    ");
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarCadastrarComValidacaoNumeroPropostaNotBlank() throws Exception {
    entradaInfoDTO.setNumeroProposta("1");
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(201));
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarCadastrarComValidacaoQtdadeParcelasNull() throws Exception {
    entradaInfoDTO.setQtdadeParcelas(null);
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarCadastrarComValidacaoQtdadeParcelasNotNull() throws Exception {
    entradaInfoDTO.setQtdadeParcelas(4);
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(201));
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarCadastrarComValidacaoQtdadeParcelasNumeroNegativo() throws Exception {
    entradaInfoDTO.setQtdadeParcelas(-2);
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarCadastrarComValidacaoQtdadeParcelasNumeroPositivo() throws Exception {
    entradaInfoDTO.setQtdadeParcelas(4);
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(201));
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarAtualizarStatusProposta() throws Exception {
    Mockito.when(infoPagamentoService.atualizarInfo(Mockito.anyString())).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(atualizarStatusDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.put("/infos/1")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(200));

    String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    RespostaAtualizacaoStatusDTO respostaAtualizada = objectMapper.readValue(jsonResposta,
        RespostaAtualizacaoStatusDTO.class);
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarExibirInfoPagamentos() throws Exception {
    Mockito.when(infoPagamentoService.exibirInfos()).thenReturn(Arrays.asList(infoPagamento));

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.get("/infos")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

    String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    List<ResumoInfoDTO> usuarios = objectMapper.readValue(jsonResposta,
        new TypeReference<List<ResumoInfoDTO>>() {
        });
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarExibirInfosPagamentoComParam() throws Exception {
    Mockito.when(infoPagamentoService.exibirInfos()).thenReturn(Arrays.asList(infoPagamento));

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.get("/infos?qtdadeDeParcelas=4")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(200))
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

    String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    List<ResumoInfoDTO> usuarios = objectMapper.readValue(jsonResposta,
        new TypeReference<List<ResumoInfoDTO>>() {
        });
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarLimiteValorExcedidoException() throws Exception {
    Mockito.doThrow(LimiteExcedidoException.class).when(infoPagamentoService).validarLimiteValorParcelas
        (Mockito.any(InfoPagamento.class));

    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders
            .post("/infos")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarPropostaJaCadastradaException() throws Exception {
    Mockito.doThrow(PropostaJaCadastradaException.class).when(infoPagamentoService).salvarInfoPagamento
        (Mockito.any(InfoPagamento.class), Mockito.anyString(), Mockito.anyInt());

    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders
            .post("/infos")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarPropostaNaoLiberadaException() throws Exception {
    Mockito.doThrow(PropostaNaoLiberadaException.class).when(infoPagamentoService).atualizarInfo(
        Mockito.anyString());

    String json = objectMapper.writeValueAsString(atualizarStatusDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders
            .put("/infos/2")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

}