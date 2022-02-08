package br.com.zup.Guardians_Bank.exceptions;

public class UsuarioNaoEcontradoException extends RuntimeException {

  public UsuarioNaoEcontradoException(String mensagem) {
    super(mensagem);
  }

}
