package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.DataInvalidaException;
import br.com.zup.Guardians_Bank.exceptions.EmAnaliseException;
import br.com.zup.Guardians_Bank.exceptions.PropostaNaoEncontradaException;
import br.com.zup.Guardians_Bank.exceptions.PropostaRecusadaException;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamento;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PropostaServiceTest {

  @MockBean
  private PropostaRepository propostaRepository;
  @MockBean
  private InfoPagamentoService infoPagamentoService;

  @Autowired
  private PropostaService propostaService;

  private Proposta proposta;
  private InfoPagamento infoPagamento;
  private Cliente cliente;

  @BeforeEach
  public void setup() {

    proposta = new Proposta();
    proposta.setNumeroProposta("1");
    proposta.setStatusProposta(StatusProposta.APROVADO);
    proposta.setValorProposta(1300.00);
    proposta.setProdutoFinanceiro(ProdutoFinanceiro.CONSIGNADO);

    infoPagamento = new InfoPagamento();
    infoPagamento.setIdPagamento("1");
    infoPagamento.setImposto(0.25);
    infoPagamento.setValorParcela(600);
    infoPagamento.setQtdadeDeParcelas(6);

    cliente = new Cliente();
    cliente.setCodcli("1");
    cliente.setCpf("75622060079");
    cliente.setNome("Dorayen");
    cliente.setSalario(3000);

  }

  @Test
  public void testarValidarStatusPropostaEmAnaliseException() {
    proposta.setStatusProposta(StatusProposta.EM_ANALISE);

    EmAnaliseException excecao = Assertions.assertThrows(EmAnaliseException.class, () -> {
      propostaService.validarStatusProposta(proposta);

    });
  }

  @Test
  public void testarValidarStatusPropostaRecusadaException() {
    proposta.setStatusProposta(StatusProposta.REPROVADO);

    PropostaRecusadaException excecao = Assertions.assertThrows(PropostaRecusadaException.class, () -> {
      propostaService.validarStatusProposta(proposta);

    });
  }

  @Test
  public void testarValidarStatusPropostaCaminhoPositivo() {
    Mockito.when(infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt())).thenReturn(infoPagamento);

    propostaService.validarStatusProposta(proposta);

    Mockito.verify(infoPagamentoService, Mockito.times(0)).salvarInfoPagamento
        (Mockito.any(InfoPagamento.class), Mockito.anyString(), Mockito.anyInt());
  }

  @Test
  public void testarValidarDataInvalidaException() {
    String data = "2021/07/07";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    LocalDate date = LocalDate.parse(data, formatter);
    proposta.setDataProposta(date);

    DataInvalidaException excecao = Assertions.assertThrows(DataInvalidaException.class, () -> {
      propostaService.validarDataContratacao(proposta);
    });
  }

  @Test //???
  public void testarValidarDataCaminhoPositivo() {
    Mockito.when(infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt())).thenReturn(infoPagamento);

    String data = "2022/01/31";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    LocalDate date = LocalDate.parse(data, formatter);
    proposta.setDataProposta(date);

    propostaService.validarDataContratacao(proposta);

    Mockito.verify(infoPagamentoService, Mockito.times(0)).salvarInfoPagamento
        (Mockito.any(InfoPagamento.class), Mockito.anyString(), Mockito.anyInt());
  }

  @Test
  public void testarValidarPropostaExistenteCaminhoPositivo() {
    Mockito.when(propostaRepository.findById(proposta.getNumeroProposta())).thenReturn(Optional.of(proposta));

    Proposta propostaValidada = propostaService.validarPropostaExistente(proposta.getNumeroProposta());
    Assertions.assertEquals(propostaValidada, proposta);

    Mockito.verify(propostaRepository, Mockito.times(1)).findById(proposta.getNumeroProposta());
  }

  @Test
  public void testarValidarPropostaExistenteCaminhoNegativo() {
    Mockito.when(propostaRepository.findById(proposta.getNumeroProposta())).thenReturn(Optional.empty());

    PropostaNaoEncontradaException excecao = Assertions.assertThrows(PropostaNaoEncontradaException.class, () -> {
      propostaService.validarPropostaExistente(proposta.getNumeroProposta());

      Mockito.verify(propostaRepository, Mockito.times(1)).findById(proposta.getNumeroProposta());
    });
  }

  @Test
  public void testarAtribuirPropostaNoInfoPagamento() {
    Mockito.when(propostaRepository.findById(proposta.getNumeroProposta())).thenReturn(Optional.of(proposta));

    testarValidarPropostaExistenteCaminhoPositivo();
    testarValidarStatusPropostaCaminhoPositivo();
    testarValidarDataCaminhoPositivo();

    InfoPagamento infoPagamento1 = propostaService.atribuirPropostaNoInfoPagamento(proposta.getNumeroProposta());

    Assertions.assertNotNull(infoPagamento1.getProposta());
  }

  @Test
  public void testarExibirListaDePagamento() {
    Mockito.when(infoPagamentoService.listarOpcoesParcelamento(infoPagamento)).thenReturn(Arrays.asList(infoPagamento));

    List<InfoPagamento> infosList = propostaService.exibirListaPagamento(infoPagamento);
    Assertions.assertNotNull(infosList);

    Mockito.verify(infoPagamentoService, Mockito.times(1)).listarOpcoesParcelamento(infoPagamento);
  }

}
