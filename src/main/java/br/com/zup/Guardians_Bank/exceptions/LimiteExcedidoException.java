package br.com.zup.Guardians_Bank.exceptions;

public class LimiteExcedidoException extends RuntimeException {

  public LimiteExcedidoException(String mensagem) {
    super(mensagem);
  }

}
