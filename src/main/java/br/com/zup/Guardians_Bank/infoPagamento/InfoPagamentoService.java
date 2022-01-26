package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.LimiteExcedidoException;
import br.com.zup.Guardians_Bank.infoPagamento.dto.RetornoPropostaDto;
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

    public InfoPagamento salvarOpcãoPagamento(InfoPagamento infoPagoOriginal, int qtidadeParcela){
        InfoPagamento infoPagamentoSalvo = infoPagoOriginal;
        infoPagamentoSalvo.getProposta().setStatusProposta(StatusProposta.LIBERADO);
        infoPagamentoSalvo.setQtdadeDeParcelas(qtidadeParcela);
        calcularValorDaParcela(infoPagamentoSalvo);
        calcularImpostoSobreParcela(infoPagamentoSalvo);
        infoPagamentoSalvo.setDataLiberacao(LocalDateTime.now());
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataPagamentoProx = dataAtual.plusDays(30);
        infoPagamentoSalvo.setDataPagamento(dataPagamentoProx);
        return infoPagamentoRepository.save(infoPagamentoSalvo);
    }
}
