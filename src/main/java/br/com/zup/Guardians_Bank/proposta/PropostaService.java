package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.DataInvalidaException;
import br.com.zup.Guardians_Bank.exceptions.EmAnaliseException;
import br.com.zup.Guardians_Bank.exceptions.PropostaRecusadaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PropostaService {

  @Autowired
  private PropostaRepository propostaRepository;

  public Proposta buscarProposta(String numeroProposta) {
    Optional<Proposta> propostaOptional = propostaRepository.findById(numeroProposta);
    return propostaOptional.get();
  }

  public Proposta validarStatusProposta(Proposta proposta) {
    if (proposta.getStatusProposta() == StatusProposta.EM_ANALISE) {
      throw new EmAnaliseException("Proposta em análise");
    }
    if (proposta.getStatusProposta() == StatusProposta.REPROVADO) {
      throw new PropostaRecusadaException("Proposta recusada");
    }
    return proposta;
  }

  public Proposta validarDataContratacao(Proposta proposta) {
    LocalDate dataAtual = LocalDate.now();

    LocalDate dataLimite = dataAtual.minusDays(90);
    if (proposta.getDataProposta().isBefore(dataLimite)) {
      throw new DataInvalidaException("Infelizmente a data deve ser inferior a 3 meses");
    }
    return proposta;

  }

//  public Proposta validarPropostaExiste(String numeroProposta){
//
//  }

}


