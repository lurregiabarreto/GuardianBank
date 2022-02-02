package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import br.com.zup.Guardians_Bank.proposta.PropostaRepository;
import br.com.zup.Guardians_Bank.proposta.PropostaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class InfoPagamentoServiceTest {
    @MockBean
    private InfoPagamentoRepository infoPagamentoRepository;
    @MockBean
    private PropostaRepository propostaRepository;
    @MockBean
    private PropostaService propostaService;

    @Autowired
    private InfoPagamentoService infoPagamentoService;

    private Proposta proposta;
    private InfoPagamento infoPagamento;
    private Cliente cliente;

    @BeforeEach
    public void setup() {

        proposta = new Proposta();
        proposta.setNumeroProposta("1");
        proposta.setStatusProposta(StatusProposta.APROVADO);
        proposta.setValorProposta(1300.00);
        proposta.setProdutoFinanceiro(ProdutoFinanceiro.CONSIGNADO);

        infoPagamento = new InfoPagamento();
        infoPagamento.setIdPagamento("1");
        infoPagamento.setImposto(0.05);
        infoPagamento.setValorParcela(600);
        infoPagamento.setQtdadeDeParcelas(6);

        cliente = new Cliente();
        cliente.setCodcli("1");
        cliente.setCpf("75622060079");
        cliente.setNome("Dorayen");
        cliente.setSalario(3000);

    }
    public InfoPagamento buscarInfoPagamento(String idPagamento) {
        Optional<InfoPagamento> infoPagamentoOptional = infoPagamentoRepository.findById(idPagamento);
        return infoPagamentoOptional.get();
    }
    @Test
    public void testarBuscarInfoPagamento(){
        Mockito.when(infoPagamentoRepository.findById(infoPagamento.getIdPagamento())).thenReturn(Optional.of(infoPagamento));
        infoPagamentoService.buscarInfoPagamento(infoPagamento.getIdPagamento());
        Assertions.assertNotNull(infoPagamento);
    }

}
