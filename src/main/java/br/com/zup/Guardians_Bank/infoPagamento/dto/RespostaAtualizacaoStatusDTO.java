package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.enums.StatusProposta;
import lombok.Data;

@Data
public class RespostaAtualizacaoStatusDTO {

    private StatusProposta statusProposta;

}
