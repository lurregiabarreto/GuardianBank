package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.LimiteExcedidoException;
import br.com.zup.Guardians_Bank.exceptions.PropostaJaCadastradaException;
import br.com.zup.Guardians_Bank.exceptions.PropostaNaoLiberadaException;
import br.com.zup.Guardians_Bank.infoPagamento.dto.RetornoInfoDTO;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import br.com.zup.Guardians_Bank.proposta.PropostaRepository;
import br.com.zup.Guardians_Bank.proposta.PropostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class InfoPagamentoService {

  @Autowired
  private InfoPagamentoRepository infoPagamentoRepository;
  @Autowired
  private PropostaRepository propostaRepository;
  @Autowired
  private PropostaService propostaService;

  public InfoPagamento salvarInfoPagamento(InfoPagamento infoPagamento, String numeroProposta, int qtdadeDeParcelas) {
    Proposta proposta = propostaService.validarPropostaExistente(numeroProposta);
    buscarInfoPorNumeroProposta(proposta.getNumeroProposta());
    propostaService.validarStatusProposta(proposta);
    propostaService.validarDataContratacao(proposta);
    infoPagamento.setProposta(proposta);
    salvarOpcaoPagamento(infoPagamento, qtdadeDeParcelas);
    return infoPagamentoRepository.save(infoPagamento);
  }

  public InfoPagamento buscarInfoPagamento(String idPagamento) {
    Optional<InfoPagamento> infoPagamentoOptional = infoPagamentoRepository.findById(idPagamento);
    return infoPagamentoOptional.get();
  }

  public boolean buscarInfoPorNumeroProposta(String numeroProposta) {
    if (infoPagamentoRepository.existsByPropostaNumeroProposta(numeroProposta)) {
      throw new PropostaJaCadastradaException("Proposta já cadastrada");
    }
    return false;
  }

  public void calcularValorDaParcela(InfoPagamento infoPagamento) {
    double jurosAa = 0;
    double valorFinanciado = infoPagamento.getProposta().getValorProposta();
    int parcelas = infoPagamento.getQtdadeDeParcelas();

    if (infoPagamento.getProposta().getProdutoFinanceiro().equals(ProdutoFinanceiro.PESSOAL)) {
      jurosAa = infoPagamento.getProposta().getProdutoFinanceiro().getTaxaDeJuros();
    }
    if (infoPagamento.getProposta().getProdutoFinanceiro().equals(ProdutoFinanceiro.CONSIGNADO)) {
      jurosAa = infoPagamento.getProposta().getProdutoFinanceiro().getTaxaDeJuros();
    }
    double jurosMes = jurosAa / 12;
    double coeficienteFinanciamento = jurosMes / (1 - (1 / Math.pow((1 + jurosMes), parcelas)));

    infoPagamento.setValorParcela(coeficienteFinanciamento * valorFinanciado);
  }

  public void calcularImpostoSobreParcela(InfoPagamento infoPagamento) {
    double imposto = 1.05;
    double valorParcelaComImposto = infoPagamento.getValorParcela() * imposto;
    BigDecimal bigDecimal2 = new BigDecimal(valorParcelaComImposto).setScale(2, RoundingMode.HALF_DOWN);
    infoPagamento.setValorParcela(bigDecimal2.doubleValue());
  }

  public double validarLimiteValorParcelas(InfoPagamento infoPagamento) {
    double salario = infoPagamento.getProposta().getCliente().getSalario();
    double limite = salario * 0.4;

    return limite;
  }

  public List<InfoPagamento> opcoesParcelamento(InfoPagamento infoPagoOriginal) {
    List<InfoPagamento> opcoesInfo = new ArrayList<InfoPagamento>();
    int parcela = 4;

    while (parcela <= 12) {
      InfoPagamento infoPagamentoatual = infoPagoOriginal;

      infoPagamentoatual.setQtdadeDeParcelas(parcela);
      calcularValorDaParcela(infoPagamentoatual);
      calcularImpostoSobreParcela(infoPagamentoatual);

      if (validarLimiteValorParcelas(infoPagamentoatual) > infoPagamentoatual.getValorParcela()) {
        opcoesInfo.add(infoPagamentoatual);
      }
      parcela = parcela + 4;

    }
    return opcoesInfo;

  }

  public InfoPagamento salvarOpcaoPagamento(InfoPagamento infoPagoOriginal, int qtdadeParcelas) {
    InfoPagamento infoPagamentoSalvo = infoPagoOriginal;
    infoPagamentoSalvo.setQtdadeDeParcelas(qtdadeParcelas);
    calcularValorDaParcela(infoPagamentoSalvo);
    calcularImpostoSobreParcela(infoPagamentoSalvo);
    infoPagamentoSalvo.setDataLiberacao(LocalDateTime.now());
    LocalDate dataAtual = LocalDate.now();
    LocalDate dataPagamentoProx = dataAtual.plusDays(30);
    infoPagamentoSalvo.setDataPagamento(dataPagamentoProx);
    return infoPagamentoSalvo;

  }

  public InfoPagamento atualizarInfo(String id) {
    InfoPagamento infoPagamento = buscarInfoPagamento(id);
    if (infoPagamento.getProposta().getStatusProposta() != StatusProposta.LIBERADO) {
      throw new PropostaNaoLiberadaException("Proposta não liberada");
    }

    infoPagamento.getProposta().setStatusProposta(StatusProposta.LIBERADO);
    infoPagamento.setDataLiberacao(LocalDateTime.now());
    infoPagamentoRepository.save(infoPagamento);
    return infoPagamento;
  }

  public List<InfoPagamento> exibirInfos() {
    List<InfoPagamento> infos = (List<InfoPagamento>) infoPagamentoRepository.findAll();
    return infos;
  }

  public List<InfoPagamento> aplicarFiltros(Integer qtdadeDeParcelas) {
    if (qtdadeDeParcelas != null) {
      return infoPagamentoRepository.findAllByQtdadeDeParcelas(qtdadeDeParcelas);
    }
    return exibirInfos();
  }

}