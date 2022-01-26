package br.com.zup.Guardians_Bank.exceptions;

public class PropostaNaoLiberadaException extends RuntimeException{

    public PropostaNaoLiberadaException(String mensagem) {
        super(mensagem);
    }
}
