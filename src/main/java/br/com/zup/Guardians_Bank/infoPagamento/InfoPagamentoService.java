package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.exceptions.LimiteExcedidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InfoPagamentoService {

    @Autowired
    private InfoPagamentoRepository infoPagamentoRepository;


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

    public List<RetornoPropostaDto> opcoesParcelamento(InfoPagamento infoPagoOriginal) {
        List<RetornoPropostaDto> opcoesParcelaDTO = new ArrayList<RetornoPropostaDto>();
        int parcela = 4;

        while (parcela <= 12) {
            InfoPagamento infoPagamentoatual = infoPagoOriginal;
            infoPagamentoatual.setQtdadeDeParcelas(parcela);
            calcularValorDaParcela(infoPagamentoatual);
            calcularImpostoSobreParcela(infoPagamentoatual);
            RetornoPropostaDto exibirParcelaDTO = new RetornoPropostaDto();
            exibirParcelaDTO.setQtidadeParcelas(parcela);
            exibirParcelaDTO.setValorParcela(infoPagamentoatual.getValorParcela());
            opcoesParcelaDTO.add(exibirParcelaDTO);
            parcela = parcela + 4;

        }

        return opcoesParcelaDTO;


    }

}
