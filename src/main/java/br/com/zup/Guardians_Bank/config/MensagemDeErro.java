package br.com.zup.Guardians_Bank.config;

import lombok.Data;

@Data
public class MensagemDeErro {

  private String mensagemDeErro;

  public MensagemDeErro(String mensagemDeErro) {
    this.mensagemDeErro = mensagemDeErro;
  }

  public String getMensagemDeErro() {
    return mensagemDeErro;
  }

  public void setMensagemDeErro(String mensagemDeErro) {
    this.mensagemDeErro = mensagemDeErro;
  }

}
