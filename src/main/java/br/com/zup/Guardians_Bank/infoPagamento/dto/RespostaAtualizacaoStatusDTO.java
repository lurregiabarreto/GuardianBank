package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.enums.TipoDeParcela;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RespostaAtualizacaoStatusDTO {

  private Proposta proposta;
  private double valorParcela;
  private Integer qtdadeDeParcelas;
  private LocalDate dataPagamento;
  private double imposto;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataLiberacao;
  private TipoDeParcela tipoDeParcela;

}
