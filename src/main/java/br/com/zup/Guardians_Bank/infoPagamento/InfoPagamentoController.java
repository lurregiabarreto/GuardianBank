package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.PropostaNaoLiberadaException;
import br.com.zup.Guardians_Bank.infoPagamento.dto.*;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import br.com.zup.Guardians_Bank.proposta.PropostaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

@RestController
@RequestMapping("/infos")
@Api(value = "Compra de Credito")
@CrossOrigin(origins = "*")
public class InfoPagamentoController {

  @Autowired
  private InfoPagamentoService infoPagamentoService;
  @Autowired
  private PropostaService propostaService;
  @Autowired
  private ModelMapper modelMapper;

  @PostMapping
  @ApiOperation(value = "Método Cadastrar informacao de pagamento")
  @ResponseStatus(HttpStatus.CREATED)
  public SaidaInfoDTO cadastrarInfoPagamento(@Valid @RequestBody EntradaInfoDTO entradaInfoDTO) {
    InfoPagamento infoPagamento = modelMapper.map(entradaInfoDTO, InfoPagamento.class);
    Proposta proposta = propostaService.buscarProposta(entradaInfoDTO.getNumeroProposta());
    infoPagamento.setProposta(proposta);
    propostaService.validarStatusProposta(proposta);
    propostaService.validarDataContratacao(proposta);
    return modelMapper.map(infoPagamentoService.salvarInfoPagamento(infoPagamento, entradaInfoDTO.getQtdadeParcelas(),
            entradaInfoDTO.getNumeroProposta()), SaidaInfoDTO.class);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Método Atualizar Informacao de Pagamento")
  @ResponseStatus(HttpStatus.OK)
  public RespostaAtualizacaoStatusDTO atualizarStatus(@PathVariable String id,
                                                      @RequestBody AtualizarStatusDTO atualizarStatusDTO) {

    if (atualizarStatusDTO.getStatusProposta() != StatusProposta.APROVADO) {
      throw new PropostaNaoLiberadaException("Proposta não liberada");

    }
    return modelMapper.map(infoPagamentoService.atualizarInfo(id), RespostaAtualizacaoStatusDTO.class);
  }

  @GetMapping
  @ApiOperation(value = "Método Exibir informacoes de pagamento por parcelamento")
  List<ResumoInfoDTO> exibirContas(@RequestParam(required = false) Integer qtdadeDeParcelas) {
    List<ResumoInfoDTO> listaDeInfos = new ArrayList<>();
    for (InfoPagamento info : infoPagamentoService.aplicarFiltros(qtdadeDeParcelas)) {
      ResumoInfoDTO resumoInfos = modelMapper.map(info, ResumoInfoDTO.class);
      listaDeInfos.add(resumoInfos);
    }
    return listaDeInfos;
  }

}
