package br.com.zup.Guardians_Bank.config;

import br.com.zup.Guardians_Bank.exceptions.DataInvalidaException;
import br.com.zup.Guardians_Bank.exceptions.EmAnaliseException;
import br.com.zup.Guardians_Bank.exceptions.PropostaRecusadaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class ControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public HashMap<String, HashMap<String, String>> manipulandoValidacao(MethodArgumentNotValidException exception) {
    HashMap<String, HashMap<String, String>> resposta = new HashMap<>();

    for (FieldError error : exception.getFieldErrors()) {
      HashMap<String, String> mensagem = construirResposta(error.getDefaultMessage());
      resposta.put(error.getField(), mensagem);
    }
    return resposta;
  }


  private HashMap<String, String> construirResposta(String mensagem) {
    HashMap<String, String> resposta = new HashMap<>();
    resposta.put("mensagem", mensagem);
    return resposta;
  }

  @ExceptionHandler(EmAnaliseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public MensagemDeErro emAnaliseException(EmAnaliseException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());
  }

  @ExceptionHandler(PropostaRecusadaException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public MensagemDeErro propostaRecusadaException(PropostaRecusadaException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());
  }

  @ExceptionHandler(DataInvalidaException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public MensagemDeErro manipularDataPosteriorException(DataInvalidaException exception) {
    return new MensagemDeErro(exception.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public MensagemDeErro enumInvalidoException(HttpMessageNotReadableException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public MensagemDeErro limiteExcedidoException(HttpMessageNotReadableException excecao) {
    return new MensagemDeErro(excecao.getLocalizedMessage());
  }
}
