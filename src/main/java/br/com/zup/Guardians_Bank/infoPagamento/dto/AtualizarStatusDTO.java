package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.enums.TipoDeParcela;
import lombok.Data;


@Data
public class AtualizarStatusDTO {

  private Integer qtdadeDeParcelas;
  private TipoDeParcela tipoDeParcela;

}
