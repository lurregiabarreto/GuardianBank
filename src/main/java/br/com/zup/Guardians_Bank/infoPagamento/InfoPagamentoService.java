package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoPagamentoService {

  @Autowired
  private InfoPagamentoRepository infoPagamentoRepository;

   public void calcularJuros(Proposta proposta, InfoPagamento infoPagamento){
     double taxa = 0;
     double principal = proposta.getValorProposta();
     int meses = infoPagamento.getQtdadeDeParcelas();

     if (proposta.getProdutoFinanceiro().equals(ProdutoFinanceiro.PESSOAL)){
       taxa = proposta.getProdutoFinanceiro().getTaxaDeJuros();
     }if (proposta.getProdutoFinanceiro().equals(ProdutoFinanceiro.CONSIGNADO)){
       taxa = proposta.getProdutoFinanceiro().getTaxaDeJuros();
     }
     double montante = principal * Math.pow((1 + taxa), meses);
     double juros = montante - principal;

   }
}
