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
  @ApiOperation(value = "Método responsável por cadastrar informações de pagamento")
  @ResponseStatus(HttpStatus.CREATED)
  public SaidaInfoDTO cadastrarInfoPagamento(@Valid @RequestBody EntradaInfoDTO entradaInfoDTO) {
    InfoPagamento infoPagamento = modelMapper.map(entradaInfoDTO, InfoPagamento.class);
    return modelMapper.map(infoPagamentoService.salvarInfoPagamento(infoPagamento, entradaInfoDTO.getNumeroProposta(),
        entradaInfoDTO.getQtdadeParcelas()), SaidaInfoDTO.class);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Método responsável por atualizar informações de pagamento")
  @ResponseStatus(HttpStatus.OK)
  public RespostaAtualizacaoStatusDTO atualizarStatus(@PathVariable String id,
                                                      @RequestBody AtualizarStatusDTO atualizarStatusDTO) {
    InfoPagamento infoPagamento = modelMapper.map(atualizarStatusDTO, InfoPagamento.class);
    return modelMapper.map(infoPagamentoService.atualizarInfo(id), RespostaAtualizacaoStatusDTO.class);
  }

  @GetMapping
  @ApiOperation(value = "Método responsável por exibir informações de pagamento por parâmetro ou não")
  List<ResumoInfoDTO> exibirInfoPagamento(@RequestParam(required = false) Integer qtdadeDeParcelas) {
    List<ResumoInfoDTO> listaDeInfos = new ArrayList<>();
    for (InfoPagamento info : infoPagamentoService.aplicarFiltros(qtdadeDeParcelas)) {
      ResumoInfoDTO resumoInfos = modelMapper.map(info, ResumoInfoDTO.class);
      listaDeInfos.add(resumoInfos);
    }
    return listaDeInfos;
  }

}
