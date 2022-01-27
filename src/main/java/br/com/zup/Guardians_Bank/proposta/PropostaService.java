package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.cliente.Cliente;
import br.com.zup.Guardians_Bank.enums.StatusProposta;
import br.com.zup.Guardians_Bank.exceptions.DataInvalidaException;
import br.com.zup.Guardians_Bank.exceptions.EmAnaliseException;
import br.com.zup.Guardians_Bank.exceptions.PropostaNaoEncontradaException;
import br.com.zup.Guardians_Bank.exceptions.PropostaRecusadaException;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamento;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropostaService {

    @Autowired
    private PropostaRepository propostaRepository;
    @Autowired
    private InfoPagamentoService infoPagamentoService;

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

    public Proposta validarPropostaExiste(String numeroProposta) {
        Optional<Proposta> propostaOptional = propostaRepository.findById(numeroProposta);
        if (propostaOptional.isEmpty()) {
            throw new PropostaNaoEncontradaException("Está proposta não foi encontrada");
        }
        Proposta proposta = propostaOptional.get();
        return proposta;
    }

    public InfoPagamento trazerInfoPorNumProposta(String id) {
        InfoPagamento infoPagamento = new InfoPagamento();

        Proposta proposta = buscarProposta(id);
        infoPagamento.setProposta(proposta);
        return infoPagamento;
    }

}
