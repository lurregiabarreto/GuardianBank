package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.config.security.TokenProvider;
import br.com.zup.Guardians_Bank.enums.ProdutoFinanceiro;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PropostaServiceTest {

  @MockBean
  private PropostaRepository propostaRepository;

  @Autowired
  private PropostaService propostaService;
  @Autowired
  private TokenProvider tokenProvider;

  private Proposta proposta;

  @BeforeEach
  public void setUp() {
    Proposta proposta = new Proposta();
    proposta.setDataProposta(proposta.getDataProposta());
    proposta.setNumeroProposta("1");
    proposta.setValorProposta(1500.00);
    proposta.setStatusProposta(StatusProposta.APROVADO);
    proposta.setCliente(proposta.getCliente());
    proposta.setProdutoFinanceiro(ProdutoFinanceiro.CONSIGNADO);

    /*TokenProvider tokenProvider = new TokenProvider();
    tokenProvider.pegarUsuarioToken("366");*/
  }

  @Test
  //@WithMockUser
  public void testarCadastroDeProposta() {
    Mockito.when(propostaRepository.save(Mockito.any(Proposta.class))).thenReturn(proposta);

    propostaService.salvarProposta(proposta);

    Mockito.verify(propostaRepository, Mockito.times(1)).save(proposta);
  }

}