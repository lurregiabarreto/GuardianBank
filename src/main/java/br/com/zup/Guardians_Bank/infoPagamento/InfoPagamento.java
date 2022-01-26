package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
  @OneToOne(cascade = CascadeType.ALL)
  private Proposta proposta;
  private double valorParcela;
  private Integer qtdadeDeParcelas;
  private LocalDate dataPagamento;
  private double imposto = 1.05;
  private LocalDateTime dataLiberacao;

}
