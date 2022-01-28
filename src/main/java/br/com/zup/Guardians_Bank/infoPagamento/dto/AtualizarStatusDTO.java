package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.enums.StatusProposta;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AtualizarStatusDTO {

  private StatusProposta statusProposta;

}
