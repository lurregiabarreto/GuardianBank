package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.PropostaNaoLiberadaException;
import br.com.zup.Guardians_Bank.infoPagamento.dto.*;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import br.com.zup.Guardians_Bank.proposta.PropostaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

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
  public SaidaInfoDTO cadastrarInfoPagamento(@RequestBody EntradaInfoDTO entradaInfoDTO) {
    InfoPagamento infoPagamento = modelMapper.map(entradaInfoDTO, InfoPagamento.class);
    Proposta proposta = propostaService.buscarProposta(entradaInfoDTO.getNumeroProposta());
    infoPagamento.setProposta(proposta);
    propostaService.validarStatusProposta(proposta);
    propostaService.validarDataContratacao(proposta);
    return modelMapper.map(infoPagamentoService.salvarInfoPagamento(infoPagamento, entradaInfoDTO.getQtdadeParcelas()),
        SaidaInfoDTO.class);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public RespostaAtualizacaoStatusDTO atualizarStatus(@PathVariable String id, @RequestBody AtualizarStatusDTO atualizarStatusDTO) {

    if (atualizarStatusDTO.getStatusProposta() != StatusProposta.APROVADO) {
      throw new PropostaNaoLiberadaException("Proposta não liberada");

    }
    return modelMapper.map(infoPagamentoService.atualizarInfo(id), RespostaAtualizacaoStatusDTO.class);
  }

  @GetMapping
  List<ResumoInfoDTO> exibirContas(@RequestParam(required = false) Integer qtdadeDeParcelas) {
    List<ResumoInfoDTO> listaDeInfos = new ArrayList<>();
    for (InfoPagamento info : infoPagamentoService.aplicarFiltros(qtdadeDeParcelas)) {
      ResumoInfoDTO resumoInfos = modelMapper.map(info, ResumoInfoDTO.class);
      listaDeInfos.add(resumoInfos);
    }
    return listaDeInfos;
  }

}
