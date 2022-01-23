package br.com.zup.Guardians_Bank.infoPagamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/infos")
public class InfoPagamentoController {

  @Autowired
  private InfoPagamentoService infoPagamentoService;

}
