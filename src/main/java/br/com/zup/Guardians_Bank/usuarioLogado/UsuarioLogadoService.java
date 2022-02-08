package br.com.zup.Guardians_Bank.usuarioLogado;

import br.com.zup.Guardians_Bank.config.Security.UsuarioLogado;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioLogadoService {

  public String pegarId() {

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    UsuarioLogado usuarioLogado = (UsuarioLogado) principal;
    return usuarioLogado.getId();
  }

}
