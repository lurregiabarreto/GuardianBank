package br.com.zup.Guardians_Bank.enums;

public enum ProdutoFinanceiro {

  PESSOAL(1.19),
  CONSIGNADO(0.22);

  private double taxaDeJuros;

  ProdutoFinanceiro(double taxaDeJuros) {
    this.taxaDeJuros = taxaDeJuros;
  }

  public double getTaxaDeJuros() {
    return taxaDeJuros;
  }

  public void setTaxaDeJuros(double taxaDeJuros) {
    this.taxaDeJuros = taxaDeJuros;
  }

}
