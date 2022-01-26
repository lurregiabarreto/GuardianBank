package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.infoPagamento.dto.EntradaInfoDTO;
import br.com.zup.Guardians_Bank.infoPagamento.dto.SaidaInfoDTO;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import br.com.zup.Guardians_Bank.proposta.PropostaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
  public SaidaInfoDTO cadastrarInfoPagamento (@RequestBody EntradaInfoDTO entradaInfoDTO) {
    InfoPagamento infoPagamento = modelMapper.map(entradaInfoDTO, InfoPagamento.class);
    Proposta proposta = propostaService.buscarProposta(entradaInfoDTO.getNumeroProposta());
    propostaService.validarStatusProposta(proposta);
    propostaService.validarDataContratacao(proposta);
    return modelMapper.map(infoPagamentoService.salvarInfoPagamento(infoPagamento, entradaInfoDTO.getQtdadeParcelas()),
        SaidaInfoDTO.class);
  }



}
