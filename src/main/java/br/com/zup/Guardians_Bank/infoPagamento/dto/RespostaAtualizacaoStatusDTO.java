package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.enums.StatusProposta;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RespostaAtualizacaoStatusDTO {

    private StatusProposta statusProposta;

}
