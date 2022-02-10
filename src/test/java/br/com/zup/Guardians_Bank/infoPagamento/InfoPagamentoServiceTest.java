package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.enums.TipoDeParcela;
import br.com.zup.Guardians_Bank.exceptions.ParcelaEspecialNaoPermitidaException;
import br.com.zup.Guardians_Bank.exceptions.PropostaJaCadastradaException;
import br.com.zup.Guardians_Bank.exceptions.PropostaNaoLiberadaException;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import br.com.zup.Guardians_Bank.proposta.PropostaRepository;
import br.com.zup.Guardians_Bank.proposta.PropostaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class InfoPagamentoServiceTest {

  @MockBean
  private InfoPagamentoRepository infoPagamentoRepository;
  @MockBean
  private PropostaRepository propostaRepository;
  @MockBean
  private PropostaService propostaService;

  @Autowired
  private InfoPagamentoService infoPagamentoService;

  private Proposta proposta;
  private InfoPagamento infoPagamento;
  private Cliente cliente;
  private Cliente clientePobre;

  @BeforeEach
  public void setup() {

    proposta = new Proposta();
    proposta.setNumeroProposta("1");
    proposta.setStatusProposta(StatusProposta.APROVADO);
    proposta.setValorProposta(1300.00);
    proposta.setProdutoFinanceiro(ProdutoFinanceiro.CONSIGNADO);

    infoPagamento = new InfoPagamento();
    infoPagamento.setIdPagamento("1");
    infoPagamento.setImposto(0.05);
    infoPagamento.setQtdadeDeParcelas(4);
    infoPagamento.setDataLiberacao(LocalDateTime.now());
    infoPagamento.setProposta(proposta);
    infoPagamento.setTipoDeParcela(TipoDeParcela.REGULAR);

    cliente = new Cliente();
    cliente.setCodcli("1");
    cliente.setCpf("75622060079");
    cliente.setNome("Dorayen");
    cliente.setSalario(3000);

    clientePobre = new Cliente();
    clientePobre.setCodcli("2");
    clientePobre.setCpf("75622060079");
    clientePobre.setNome("Carola");
    clientePobre.setSalario(50);

  }

  @Test
  public void testarBuscarInfoPagamento() {
    Mockito.when(infoPagamentoRepository.findById(infoPagamento.getIdPagamento())).
        thenReturn(Optional.of(infoPagamento));
    infoPagamentoService.buscarInfoPagamentoPorId(infoPagamento.getIdPagamento());
    Assertions.assertNotNull(infoPagamento);
  }

  @Test
  public void testarCalcularValorDaParcela() {
    infoPagamento.setProposta(proposta);
    infoPagamentoService.calcularValorDaParcela(infoPagamento);
    Assertions.assertEquals(340.0311292475448, infoPagamento.getValorParcela());
    Assertions.assertNotNull(infoPagamento.getValorParcela());

  }

  @Test
  public void testarCalcularImpostoSobreParcela() {
    infoPagamento.setProposta(proposta);
    infoPagamento.setValorParcela(340.0311292475448);
    infoPagamentoService.calcularImpostoSobreParcela(infoPagamento);
    Assertions.assertEquals(357.03, infoPagamento.getValorParcela());
    Assertions.assertEquals(17.00, infoPagamento.getImposto());
    Assertions.assertNotNull(infoPagamento.getValorParcela());
    Assertions.assertNotNull(infoPagamento.getImposto());

  }

  @Test
  public void testarCalcularLimiteValoParcelas() {
    proposta.setCliente(cliente);
    infoPagamento.setProposta(proposta);
    infoPagamento.setValorParcela(357.03);
    double resultado = infoPagamentoService.calcularLimiteValorParcelas(infoPagamento);
    Assertions.assertEquals(1200, resultado);
  }

  @Test
  public void testarValidarPropostaJaCadastradaCaminhoNegativo() {
    Mockito.when(infoPagamentoRepository.existsByPropostaNumeroProposta(Mockito.anyString()))
        .thenReturn(true);
    Assertions.assertThrows(PropostaJaCadastradaException.class,
        () -> infoPagamentoService.validarPropostaJaCadastrada(proposta.getNumeroProposta()));

  }

  @Test
  public void testarValidarPropostaJaCadastradaCaminhoPositivo() {
    Mockito.when(infoPagamentoRepository.existsByPropostaNumeroProposta(Mockito.anyString()))
        .thenReturn(false);
    Boolean positivo = infoPagamentoService.validarPropostaJaCadastrada(proposta.getNumeroProposta());
    Assertions.assertFalse(positivo);
  }

  @Test
  public void testarListarOpcoesParcelamentoCaminhoPositivo() {
    proposta.setCliente(cliente);
    infoPagamento.setProposta(proposta);
    infoPagamentoService.listarOpcoesParcelamento(infoPagamento);
    List<InfoPagamento> resultado = infoPagamentoService.listarOpcoesParcelamento(infoPagamento);
    Assertions.assertNotNull(resultado);
    System.out.println(resultado);
  }

  @Test
  public void testarListarOpcoesParcelamentoCaminhoNegativo() {

    List<InfoPagamento> resultadoruim = new ArrayList<>();
    proposta.setCliente(clientePobre);
    infoPagamento.setProposta(proposta);
    infoPagamentoService.listarOpcoesParcelamento(infoPagamento);
    List<InfoPagamento> resultado = infoPagamentoService.listarOpcoesParcelamento(infoPagamento);
    Assertions.assertEquals(0, resultado.size());
    Assertions.assertEquals(resultadoruim, resultado);
  }

  @Test
  public void testarSalvarOpcaoPagamento() {
    InfoPagamento resultado = new InfoPagamento();
    resultado.setProposta(proposta);
    resultado.setTipoDeParcela(TipoDeParcela.REGULAR);
    resultado.setQtdadeDeParcelas(infoPagamento.getQtdadeDeParcelas());
    infoPagamentoService.salvarOpcaoPagamento(resultado, infoPagamento.getQtdadeDeParcelas());
    Assertions.assertEquals(357.03, resultado.getValorParcela());
    Assertions.assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
        resultado.getDataLiberacao().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
    Assertions.assertEquals(LocalDateTime.now().plusDays(30).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
        resultado.getDataPagamento().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
    Assertions.assertNotNull(resultado);

  }

  @Test
  public void testarSalvarInfoPagamentoCaminhoPositivo() {
    Mockito.when(infoPagamentoRepository.save(Mockito.any(InfoPagamento.class))).thenReturn(infoPagamento);
    Mockito.when(infoPagamentoRepository.existsByPropostaNumeroProposta(Mockito.anyString())).thenReturn(false);
    Mockito.when(propostaService.validarPropostaExistente(proposta.getNumeroProposta())).thenReturn(proposta);
    testarValidarPropostaJaCadastradaCaminhoPositivo();
    Mockito.when(propostaService.validarStatusProposta(proposta)).thenReturn(proposta);
    Mockito.when(propostaService.validarDataContratacao(proposta)).thenReturn(proposta);

    infoPagamento.setProposta(proposta);
    testarSalvarOpcaoPagamento();

    InfoPagamento infoPagamento1 = infoPagamentoService.salvarInfoPagamento(infoPagamento, infoPagamento
            .getProposta().getNumeroProposta(),
        infoPagamento.getQtdadeDeParcelas());

    Assertions.assertNotNull(infoPagamento1);
    Assertions.assertEquals(InfoPagamento.class, infoPagamento1.getClass());

    Mockito.verify(infoPagamentoRepository, Mockito.times(1)).save(infoPagamento);

  }

  @Test
  public void testarAtualizarInfoCaminhoPositivo() {
    Mockito.when(infoPagamentoRepository.save(Mockito.any(InfoPagamento.class))).thenReturn(infoPagamento);
    Mockito.when(infoPagamentoRepository.findById(Mockito.anyString())).thenReturn(Optional.of(infoPagamento));


    infoPagamento.getProposta().setStatusProposta(StatusProposta.LIBERADO);
    infoPagamento.setDataLiberacao(LocalDateTime.now());

    InfoPagamento infoPagamento1 = infoPagamentoService.atualizarInfoPagamento(infoPagamento.getIdPagamento()
        ,infoPagamento);

    Mockito.verify(infoPagamentoRepository, Mockito.times(1)).save(infoPagamento);
    Mockito.verify(infoPagamentoRepository, Mockito.times(1))
        .findById(infoPagamento.getIdPagamento());
  }

  @Test
  public void testarAtualizarInfoCaminhoNegativo() {
    Mockito.when(infoPagamentoRepository.save(Mockito.any(InfoPagamento.class))).thenReturn(infoPagamento);
    Mockito.when(infoPagamentoRepository.findById(Mockito.anyString())).thenReturn(Optional.of(infoPagamento));

    infoPagamento.getProposta().setStatusProposta(StatusProposta.EM_ANALISE);

    RuntimeException exception = Assertions.assertThrows(PropostaNaoLiberadaException.class,
        () -> infoPagamentoService.atualizarInfoPagamento(proposta.getNumeroProposta(), infoPagamento));

    Mockito.verify(infoPagamentoRepository, Mockito.times(0)).save(infoPagamento);
    Mockito.verify(infoPagamentoRepository, Mockito.times(1))
        .findById(infoPagamento.getIdPagamento());
  }

  @Test
  public void testarExibirInfosDePagamento() {
    Mockito.when(infoPagamentoRepository.findAll()).thenReturn(Arrays.asList(infoPagamento));

    List<InfoPagamento> infosList = infoPagamentoService.exibirInfosPagamento();
    Assertions.assertNotNull(infosList);

    Mockito.verify(infoPagamentoRepository, Mockito.times(1)).findAll();
  }

  @Test
  public void testarExibirInfosDePagamentoComFiltro() {
    Mockito.when(infoPagamentoRepository.findAllByQtdadeDeParcelas(infoPagamento.getQtdadeDeParcelas()))
        .thenReturn(Arrays.asList(infoPagamento));

    List<InfoPagamento> infosList = infoPagamentoService.aplicarFiltros(infoPagamento.getQtdadeDeParcelas());
    Assertions.assertNotNull(infosList);

    Mockito.verify(infoPagamentoRepository, Mockito.times(1))
        .findAllByQtdadeDeParcelas(infoPagamento.getQtdadeDeParcelas());
  }
  @Test
  public void testarValidarParcelaEspecial(){
    infoPagamento.setTipoDeParcela(TipoDeParcela.ESPECIAL);
    infoPagamento.setQtdadeDeParcelas(8);
    infoPagamentoService.validarParcelaEspecial(infoPagamento);
    Assertions.assertEquals(TipoDeParcela.REGULAR, infoPagamento.getTipoDeParcela());
    Assertions.assertEquals(LocalDate.now().plusDays(30),infoPagamento.getDataPagamento());
  }

  @Test
  public void testarValidarParcelaNaoEspecialException(){
    infoPagamento.setTipoDeParcela(TipoDeParcela.ESPECIAL);
    infoPagamento.setQtdadeDeParcelas(8);
    RuntimeException exception = Assertions.assertThrows(ParcelaEspecialNaoPermitidaException.class,
            () -> infoPagamentoService.validarParcelaNaoEspecial(infoPagamento));

  }

}
