package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.enums.TipoDeParcela;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AtualizarStatusDTO {

  private int numeroParcelas;
  private TipoDeParcela tipoDeParcela;

}
