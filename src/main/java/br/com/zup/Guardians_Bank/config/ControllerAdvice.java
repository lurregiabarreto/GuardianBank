package br.com.zup.Guardians_Bank.config;


import br.com.zup.Guardians_Bank.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public List<MensagemDeErro> tratarErrosDeValidacao(MethodArgumentNotValidException excecao) {
    List<MensagemDeErro> errosValidacao = new ArrayList<>();

    for (FieldError referencia : excecao.getFieldErrors()) {
      MensagemDeErro mensagemDeErro = new MensagemDeErro(referencia.getDefaultMessage());
      errosValidacao.add(mensagemDeErro);
    }

    return errosValidacao;
  }

  @ExceptionHandler(EmAnaliseException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public MensagemDeErro emAnaliseException(EmAnaliseException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());
  }

  @ExceptionHandler(PropostaRecusadaException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public MensagemDeErro propostaRecusadaException(PropostaRecusadaException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());
  }

  @ExceptionHandler(DataInvalidaException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public MensagemDeErro manipularDataPosteriorException(DataInvalidaException exception) {
    return new MensagemDeErro(exception.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public MensagemDeErro enumInvalidoException(HttpMessageNotReadableException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());
  }

  @ExceptionHandler(LimiteExcedidoException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public MensagemDeErro limiteExcedidoException(LimiteExcedidoException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());
  }

  @ExceptionHandler(PropostaNaoLiberadaException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public MensagemDeErro propostaNaoLiberadaException(PropostaNaoLiberadaException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());
  }

  @ExceptionHandler(PropostaNaoEncontradaException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public MensagemDeErro propostaNaoEncontrada(PropostaNaoEncontradaException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());

  }

  @ExceptionHandler(PropostaJaCadastradaException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) //ok
  public MensagemDeErro propostaJaCadastrada(PropostaJaCadastradaException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());

  }

  @ExceptionHandler(UsuarioNaoEcontradoException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND) //ok
  public MensagemDeErro propostaJaCadastrada(UsuarioNaoEcontradoException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());

  }

  @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) //ok
  public ResponseEntity enumInvalidoException(ArrayIndexOutOfBoundsException exception) {
    if (exception.getLocalizedMessage().contains("br.com.zup.Guardians_Bank.enums.ProductoFinanceiro")) {
      return ResponseEntity.status(422).build();
    }
    if (exception.getLocalizedMessage().contains("br.com.zup.Guardians_Bank.enums.StatusProposta")) {
      return ResponseEntity.status(422).build();
    }
    return ResponseEntity.status(400).build();

  }

}

