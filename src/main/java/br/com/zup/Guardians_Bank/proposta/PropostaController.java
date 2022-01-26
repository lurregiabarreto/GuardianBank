package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaService propostaService;
    @Autowired
    private InfoPagamentoService infoPagamentoService;
    @Autowired
    private ModelMapper modelMapper;

//  @GetMapping("/{id}")
//  public RetornoInfoDto exibirOpcoesPagamento(@PathVariable String id){
//
//    RetornoInfoDto retornoInfoDto = modelMapper.map(infoPagamento, RetornoInfoDto.class);
//    return retornoInfoDto;
//  }


}

