package br.com.zup.Guardians_Bank.infoPagamento.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RetornoInfoDTO {

  private int qtdadeParcelas;
  private double valorParcela;
  private LocalDate dataPagamento;
  private String parcelaEspecial;


}
