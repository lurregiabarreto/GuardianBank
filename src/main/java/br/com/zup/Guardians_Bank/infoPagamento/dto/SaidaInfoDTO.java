package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.proposta.Proposta;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SaidaInfoDTO {

  private Proposta proposta;
  private double valorParcela;
  private int qtdadeDeParcelas;
  private LocalDate dataPagamento;
  private double imposto;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataLiberacao;

}
