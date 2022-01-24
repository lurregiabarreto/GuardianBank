package br.com.zup.Guardians_Bank.cliente;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "clientes")
public class Cliente {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String codcli;
  private String nome;
  private String cpf;
  private double salario;

}
