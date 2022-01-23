package br.com.zup.Guardians_Bank.cliente;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "clientes")
public class Cliente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String codcli;
  private String nome;
  private String cpf;
  private double salario;

}
