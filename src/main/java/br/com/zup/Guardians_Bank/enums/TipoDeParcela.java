package br.com.zup.Guardians_Bank.enums;

import java.time.LocalDate;

public enum TipoDeParcela {
    REGULAR(LocalDate.now().plusDays(30)),
    ESPECIAL(LocalDate.now().plusDays(45));

    private LocalDate dataPagamento;

    TipoDeParcela(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
