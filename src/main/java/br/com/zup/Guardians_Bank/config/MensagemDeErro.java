package br.com.zup.Guardians_Bank.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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

