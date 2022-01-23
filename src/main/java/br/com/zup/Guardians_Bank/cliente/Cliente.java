package br.com.zup.Guardians_Bank.cliente;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "clientes")
public class Cliente {

  private String codcli;
  private String nome;
  private String cpf;
  private double salario;

}
