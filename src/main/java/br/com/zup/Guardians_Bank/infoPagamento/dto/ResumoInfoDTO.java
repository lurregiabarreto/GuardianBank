package br.com.zup.Guardians_Bank.infoPagamento.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResumoInfoDTO {

  private double valorParcela;
  private Integer qtdadeDeParcelas;
  private LocalDate dataPagamento;
  private LocalDateTime dataLiberacao;

}
