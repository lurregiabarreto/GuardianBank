package br.com.zup.Guardians_Bank.infoPagamento.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class EntradaInfoDTO {

  @NotBlank(message = "{validacao.not-blank}")
  private String numeroProposta;
  @NotNull(message = "{validacao.not-null}")
  @Positive(message = "{validacao.positive}")
  private Integer qtdadeParcelas;

}
