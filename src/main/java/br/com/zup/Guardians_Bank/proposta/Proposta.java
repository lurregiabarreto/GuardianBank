package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamento;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name="propostas")
public class Proposta {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String numeroProposta;
  private LocalDate dataProposta;
  private ProdutoFinanceiro produtoFinanceiro;
  private double valorProposta;
  @OneToOne
  private Cliente cliente;
  private StatusProposta statusProposta;

}
