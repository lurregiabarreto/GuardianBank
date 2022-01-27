package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.LimiteExcedidoException;
import br.com.zup.Guardians_Bank.infoPagamento.dto.RetornoInfoDTO;
import br.com.zup.Guardians_Bank.proposta.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public InfoPagamento buscarInfoPagamento(String idPagamento) {
    Optional<InfoPagamento> infoPagamentoOptional = infoPagamentoRepository.findById(idPagamento);
    return infoPagamentoOptional.get();
  }

  public InfoPagamento salvarInfoPagamento(InfoPagamento infoPagamento, int qtdadeDeParcelas) {
    salvarOpcaoPagamento(infoPagamento, qtdadeDeParcelas);
    return infoPagamentoRepository.save(infoPagamento);
  }

  public void calcularValorDaParcela(InfoPagamento infoPagamento) {
    double juros = 0;
    double valorFinanciado = infoPagamento.getProposta().getValorProposta();
    int parcelas = infoPagamento.getQtdadeDeParcelas();

    if (infoPagamento.getProposta().getProdutoFinanceiro().equals(ProdutoFinanceiro.PESSOAL)) {
      juros = infoPagamento.getProposta().getProdutoFinanceiro().getTaxaDeJuros();
    }
    if (infoPagamento.getProposta().getProdutoFinanceiro().equals(ProdutoFinanceiro.CONSIGNADO)) {
      juros = infoPagamento.getProposta().getProdutoFinanceiro().getTaxaDeJuros();
    }
    double coeficienteFinanciamento = juros / (1 - (1 / ((Math.pow((1 + juros), parcelas)))));

    infoPagamento.setValorParcela(coeficienteFinanciamento * valorFinanciado);
  }

  public void calcularImpostoSobreParcela(InfoPagamento infoPagamento) {
    double valorTotal = 0;
    valorTotal = infoPagamento.getValorParcela() * infoPagamento.getImposto();
    infoPagamento.setValorParcela(valorTotal);
  }

  public InfoPagamento validarLimiteValorParcelas(InfoPagamento infoPagamento) {
    double salario = infoPagamento.getProposta().getCliente().getSalario();
    double limite = salario * 0.4;

    if (infoPagamento.getValorParcela() > limite) {
      throw new LimiteExcedidoException("O valor da parcela excede limite permitido");
    }
    return infoPagamento;
  }

  public List<RetornoInfoDTO> opcoesParcelamento(InfoPagamento infoPagoOriginal) {
    List<RetornoInfoDTO> opcoesParcelaDTO = new ArrayList<RetornoInfoDTO>();
    int parcela = 4;

    while (parcela <= 12) {
      InfoPagamento infoPagamentoatual = infoPagoOriginal;
      infoPagamentoatual.setQtdadeDeParcelas(parcela);
      calcularValorDaParcela(infoPagamentoatual);
      calcularImpostoSobreParcela(infoPagamentoatual);
      RetornoInfoDTO exibirParcelaDTO = new RetornoInfoDTO();
      exibirParcelaDTO.setQtidadeParcelas(parcela);
      exibirParcelaDTO.setValorParcela(infoPagamentoatual.getValorParcela());
      opcoesParcelaDTO.add(exibirParcelaDTO);
      parcela = parcela + 4;

    }

    return opcoesParcelaDTO;

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