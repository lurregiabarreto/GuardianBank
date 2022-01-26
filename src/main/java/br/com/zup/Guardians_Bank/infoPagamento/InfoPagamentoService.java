package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.LimiteExcedidoException;
import br.com.zup.Guardians_Bank.infoPagamento.dto.RetornoInfoDto;
import br.com.zup.Guardians_Bank.proposta.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class InfoPagamentoService {

    @Autowired
    private InfoPagamentoRepository infoPagamentoRepository;
    @Autowired
    private PropostaRepository propostaRepository;

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

    public List<RetornoInfoDto> opcoesParcelamento(InfoPagamento infoPagoOriginal) {
        List<RetornoInfoDto> opcoesParcelaDTO = new ArrayList<RetornoInfoDto>();
        int parcela = 4;

        while (parcela <= 12) {
            InfoPagamento infoPagamentoatual = infoPagoOriginal;
            infoPagamentoatual.setQtdadeDeParcelas(parcela);
            calcularValorDaParcela(infoPagamentoatual);
            calcularImpostoSobreParcela(infoPagamentoatual);
            RetornoInfoDto exibirParcelaDTO = new RetornoInfoDto();
            exibirParcelaDTO.setQtidadeParcelas(parcela);
            exibirParcelaDTO.setValorParcela(infoPagamentoatual.getValorParcela());
            opcoesParcelaDTO.add(exibirParcelaDTO);
            parcela = parcela + 4;

        }

        return opcoesParcelaDTO;

    }

    public InfoPagamento salvarOpcaoPagamento(InfoPagamento infoPagoOriginal, int qtdadeParcelas) {
        InfoPagamento infoPagamentoSalvo = infoPagoOriginal;
        infoPagamentoSalvo.getProposta().setStatusProposta(StatusProposta.LIBERADO);
        infoPagamentoSalvo.setQtdadeDeParcelas(qtdadeParcelas);
        calcularValorDaParcela(infoPagamentoSalvo);
        calcularImpostoSobreParcela(infoPagamentoSalvo);
        infoPagamentoSalvo.setDataLiberacao(LocalDateTime.now());
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataPagamentoProx = dataAtual.plusDays(30);
        infoPagamentoSalvo.setDataPagamento(dataPagamentoProx);
        return infoPagamentoSalvo;
    }
}

