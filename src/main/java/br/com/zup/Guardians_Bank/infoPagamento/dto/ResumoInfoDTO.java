package br.com.zup.Guardians_Bank.infoPagamento.dto;

import br.com.zup.Guardians_Bank.enums.TipoDeParcela;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResumoInfoDTO {
    private String numeroProposta;
    private String idPagamento;
    private double valorParcela;
    private Integer qtdadeDeParcelas;
    private LocalDate dataPagamento;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataLiberacao;
    private TipoDeParcela tipoDeParcela;

}
