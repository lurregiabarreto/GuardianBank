package br.com.zup.Guardians_Bank.infoPagamento.dto;

public class RetornoInfoDto {

    int QtidadeParcelas;
    double valorParcela;

    public RetornoInfoDto() {

    }

    public int getQtidadeParcelas() {
        return QtidadeParcelas;
    }

    public void setQtidadeParcelas(int qtidadeParcelas) {
        QtidadeParcelas = qtidadeParcelas;
    }

    public double getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(double valorParcela) {
        this.valorParcela = valorParcela;
    }
}
