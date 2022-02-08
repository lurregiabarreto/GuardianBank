package br.com.zup.Guardians_Bank.proposta.dtos;

import br.com.zup.Guardians_Bank.infoPagamento.dto.RetornoInfoDTO;
import lombok.Data;

import java.util.List;
@Data
public class OpcoesPagamentoDTO {

    private String numeroDaProposta;
    private String codcli;
    private List<RetornoInfoDTO> opcoes;

}
