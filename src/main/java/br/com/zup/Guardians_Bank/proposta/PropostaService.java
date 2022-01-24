package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.DataInvalidaException;
import br.com.zup.Guardians_Bank.exceptions.EmAnaliseException;
import br.com.zup.Guardians_Bank.exceptions.PropostaRecusadaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PropostaService {

  @Autowired
  private PropostaRepository propostaRepository;

  public Proposta salvarProposta(Proposta proposta) {
    return propostaRepository.save(proposta);
  }

  public void validarStatusProposta(Proposta proposta) {
    if (proposta.getStatusProposta() == StatusProposta.EM_ANALISE) {
      throw new EmAnaliseException("Proposta em análise");
    }
    if (proposta.getStatusProposta() == StatusProposta.REPROVADO) {
      throw new PropostaRecusadaException("Proposta recusada");
    }
    salvarProposta(proposta);
  }

  public void validarDataContratacao(Proposta proposta) {
    LocalDate dataAtual = LocalDate.now();

    LocalDate dataLimite = dataAtual.minusDays(90);
    if (proposta.getDataProposta().isBefore(dataLimite)) {
      throw new DataInvalidaException("Infelizmente a data deve ser inferior a 3 meses");

    }
  }
}


