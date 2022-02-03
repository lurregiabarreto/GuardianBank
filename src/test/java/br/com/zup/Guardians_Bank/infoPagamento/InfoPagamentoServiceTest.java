package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.PropostaJaCadastradaException;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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
    private Cliente clientePobre;

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
        infoPagamento.setQtdadeDeParcelas(4);

        cliente = new Cliente();
        cliente.setCodcli("1");
        cliente.setCpf("75622060079");
        cliente.setNome("Dorayen");
        cliente.setSalario(3000);

        clientePobre = new Cliente();
        clientePobre.setCodcli("2");
        clientePobre.setCpf("75622060079");
        clientePobre.setNome("Carola");
        clientePobre.setSalario(50);

    }

    @Test
    public void testarBuscarInfoPagamento() {
        Mockito.when(infoPagamentoRepository.findById(infoPagamento.getIdPagamento())).
                thenReturn(Optional.of(infoPagamento));
        infoPagamentoService.buscarInfoPagamento(infoPagamento.getIdPagamento());
        Assertions.assertNotNull(infoPagamento);
    }

    @Test
    public void testarCalcularValorDaParcela() {
        infoPagamento.setProposta(proposta);
        infoPagamentoService.calcularValorDaParcela(infoPagamento);
        Assertions.assertEquals(340.0311292475448, infoPagamento.getValorParcela());
        Assertions.assertNotNull(infoPagamento.getValorParcela());

    }

    @Test
    public void testarCalcularImpostoSobreParcela() {
        infoPagamento.setProposta(proposta);
        infoPagamento.setValorParcela(340.0311292475448);
        infoPagamentoService.calcularImpostoSobreParcela(infoPagamento);
        Assertions.assertEquals(357.03, infoPagamento.getValorParcela());
        Assertions.assertEquals(17.00, infoPagamento.getImposto());
        Assertions.assertNotNull(infoPagamento.getValorParcela());
        Assertions.assertNotNull(infoPagamento.getImposto());

    }

    @Test
    public void testarCalcularLimiteValoParcelas() {
        proposta.setCliente(cliente);
        infoPagamento.setProposta(proposta);
        infoPagamento.setValorParcela(357.03);
        double resultado = infoPagamentoService.calcularLimiteValorParcelas(infoPagamento);
        Assertions.assertEquals(1200, resultado);
    }

    @Test
    public void testarBuscarInfoPorNumeroPropostaCaminhoNegativo() {
        Mockito.when(infoPagamentoRepository.existsByPropostaNumeroProposta(Mockito.anyString()))
                .thenReturn(true);
        Assertions.assertThrows(PropostaJaCadastradaException.class,
                () -> infoPagamentoService.buscarInfoPorNumeroProposta(proposta.getNumeroProposta()));

    }

    @Test
    public void testarBuscarInfoPorNumeroPropostaCaminhoPositivo() {
        Mockito.when(infoPagamentoRepository.existsByPropostaNumeroProposta(Mockito.anyString()))
                .thenReturn(false);
        Boolean positivo = infoPagamentoService.buscarInfoPorNumeroProposta(proposta.getNumeroProposta());
        Assertions.assertFalse(positivo);
    }

    @Test
    public void testarOpcoesParcelamentoCaminhoPositivo() {
        proposta.setCliente(cliente);
        infoPagamento.setProposta(proposta);
        infoPagamentoService.opcoesParcelamento(infoPagamento);
        List<InfoPagamento> resultado = infoPagamentoService.opcoesParcelamento(infoPagamento);
        Assertions.assertNotNull(resultado);

    }

}
