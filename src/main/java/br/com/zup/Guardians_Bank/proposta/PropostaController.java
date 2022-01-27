package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamento;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoService;

import br.com.zup.Guardians_Bank.proposta.dtos.OpcoesPagamentoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    @GetMapping("/{id}")
    public OpcoesPagamentoDTO exibirOpcoesPagamento(@PathVariable String id) {
        propostaService.validarPropostaExiste(id);
        InfoPagamento infoPagamento = propostaService.trazerInfoPorNumProposta(id);
        OpcoesPagamentoDTO opcoesPagamentoDTO = new OpcoesPagamentoDTO();
        opcoesPagamentoDTO.setOpcoes(infoPagamentoService.opcoesParcelamento(infoPagamento));
        return opcoesPagamentoDTO;
    }


}

