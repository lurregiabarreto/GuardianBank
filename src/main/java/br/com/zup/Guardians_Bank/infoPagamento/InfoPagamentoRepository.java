package br.com.zup.Guardians_Bank.infoPagamento;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InfoPagamentoRepository extends CrudRepository<InfoPagamento, String> {

  List<InfoPagamento> findAllByQtdadeDeParcelas(Integer qtdadeDeParcelas);

  boolean existsByPropostaNumeroProposta(String numeroProposta);

}
