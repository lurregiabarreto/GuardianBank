package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "infoPagamentos")
public class InfoPagamento {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String idPagamento;
  private Proposta proposta;
  private double valorParcela;
  private int qtdadeDeParcelas;
  private LocalDate dataPagamento;
  private Cliente cliente;
  private ProdutoFinanceiro produtoFinanceiro;
  private double imposto;
  private LocalDateTime dataLiberacao;

}
