package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.enums.StatusProposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropostaService {

  @Autowired
  private PropostaRepository propostaRepository;

  public Proposta salvarProposta(Proposta proposta) {
    return propostaRepository.save(proposta);
  }

  public void validarStatusProposta(Proposta proposta) {
    if (proposta.getStatusProposta() == StatusProposta.EM_ANALISE) {
      throw new RuntimeException(); //criar Em Analise Exception
    }
    if (proposta.getStatusProposta() == StatusProposta.REPROVADO) {
      throw new RuntimeException(); //criar Proposta Recusada Exception
    }
    salvarProposta(proposta);
  }

}


