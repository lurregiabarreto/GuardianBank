package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.EmAnaliseException;
import br.com.zup.Guardians_Bank.exceptions.PropostaRecusadaException;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamento;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoService;
import br.com.zup.Guardians_Bank.proposta.dtos.OpcoesPagamentoDTO;
import br.com.zup.Guardians_Bank.usuario.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PropostaServiceTest {

  @MockBean
  private PropostaRepository propostaRepository;
  @MockBean
  private InfoPagamentoService infoPagamentoService;

  @Autowired
  private PropostaService propostaService;

  private Proposta proposta;
  private Usuario usuario;
  private InfoPagamento infoPagamento;
  private Cliente cliente;
  private OpcoesPagamentoDTO opcoesPagamentoDTO;

  @BeforeEach
  public void setup() {

    proposta = new Proposta();
    proposta.setNumeroProposta("1");
    proposta.setStatusProposta(StatusProposta.APROVADO);
    proposta.setValorProposta(1300.00);
    proposta.setProdutoFinanceiro(ProdutoFinanceiro.CONSIGNADO);

    infoPagamento = new InfoPagamento();
    infoPagamento.setIdPagamento("1");
    infoPagamento.setImposto(0.25);
    infoPagamento.setValorParcela(600);
    infoPagamento.setQtdadeDeParcelas(6);

    cliente = new Cliente();
    cliente.setCodcli("1");

  }

  @Test
  public void validarStatusPropostaEmAnaliseException() {
    proposta.setStatusProposta(StatusProposta.EM_ANALISE);

    EmAnaliseException excecao = Assertions.assertThrows(EmAnaliseException.class, () -> {
      propostaService.validarStatusProposta(proposta);

    });
  }

  @Test
  public void validarStatusPropostaRecusadaException() {
    proposta.setStatusProposta(StatusProposta.REPROVADO);

    PropostaRecusadaException excecao = Assertions.assertThrows(PropostaRecusadaException.class, () -> {
      propostaService.validarStatusProposta(proposta);

    });
  }

}