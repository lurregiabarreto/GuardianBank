package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.proposta.dtos.EntradaPropostaDTO;
import lombok.Data;

@Data
public class EntradaInfoDTO {

  private EntradaPropostaDTO entradaPropostaDTO;
  private int qtdadeDeParcelas;

}
