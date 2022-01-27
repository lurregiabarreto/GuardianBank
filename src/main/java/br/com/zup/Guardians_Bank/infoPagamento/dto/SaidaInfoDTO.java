package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.proposta.Proposta;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SaidaInfoDTO {

  private Proposta proposta;
  private double valorParcela;
  private int qtdadeDeParcelas;
  private LocalDate dataPagamento;
  private double imposto;
  private LocalDateTime dataLiberacao;

}
