package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
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
  @Column(nullable = false)
  @DecimalMin(value = "0.00", inclusive = false)
  @Digits(integer = 10, fraction = 2)
  private double valorParcela;
  @Column(nullable = false)
  private Integer qtdadeDeParcelas;
  @Column(nullable = false)
  private LocalDate dataPagamento;
  @Column(nullable = false)
  private double imposto;
  @Column(nullable = false)
  private LocalDateTime dataLiberacao;

}
