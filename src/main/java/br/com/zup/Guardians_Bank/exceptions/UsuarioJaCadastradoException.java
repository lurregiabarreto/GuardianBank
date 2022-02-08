package br.com.zup.Guardians_Bank.exceptions;

public class UsuarioJaCadastradoException extends RuntimeException {

  public UsuarioJaCadastradoException(String mensagem) {
    super(mensagem);
  }

}
