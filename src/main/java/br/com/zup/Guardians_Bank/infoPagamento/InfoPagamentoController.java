package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.proposta.PropostaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/infos")
public class InfoPagamentoController {

  @Autowired
  private InfoPagamentoService infoPagamentoService;
  @Autowired
  private PropostaService propostaService;
  @Autowired
  private ModelMapper modelMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public



}
