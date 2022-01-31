package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamento;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoService;

import br.com.zup.Guardians_Bank.proposta.dtos.OpcoesPagamentoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/propostas")
@Api(value = "Comunicacao Externa para a Compra de Credito")
@CrossOrigin(origins = "*")
public class PropostaController {

    @Autowired
    private PropostaService propostaService;
    @Autowired
    private InfoPagamentoService infoPagamentoService;
    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("/{id}")
    @ApiOperation(value = "Método responsável por exibir as opções de parcelamento")
    public OpcoesPagamentoDTO exibirOpcoesPagamento(@PathVariable String id) {
        OpcoesPagamentoDTO opcoesPagamentoDTO = propostaService.exibirOpcoesValidadas(id);
        return opcoesPagamentoDTO;
    }


}

