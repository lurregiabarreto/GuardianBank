package br.com.zup.Guardians_Bank.usuario;

import br.com.zup.Guardians_Bank.exceptions.UsuarioJaCadastradoException;
import br.com.zup.Guardians_Bank.exceptions.UsuarioNaoEcontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;
  @Autowired
  private BCryptPasswordEncoder encoder;

  public Usuario salvarUsuario(Usuario usuario) {
    if (encontrarUsuarioPorEmail(usuario.getEmail())) {
      throw new UsuarioJaCadastradoException("Usuário já cadastrado");
    } else {
      String senhaEscondida = encoder.encode(usuario.getSenha());

      usuario.setSenha(senhaEscondida);
      usuarioRepository.save(usuario);
      return usuario;
    }

  }

  public boolean encontrarUsuarioPorEmail(String email) {
    Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
    if (!usuarioOptional.isEmpty()) {
      return true;
    } else {
      return false;
    }

  }

  public List<Usuario> buscarTodosOsUsuarios() {
    Iterable<Usuario> usuarios = usuarioRepository.findAll();
    return (List<Usuario>) usuarios;
  }

  public Usuario buscarUsuarioPorId(String id) {
    Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
    if (!usuarioOptional.isEmpty()) {
      return usuarioOptional.get();
    } else {
      throw new UsuarioNaoEcontradoException("Usuário não encontrado");
    }

  }

  public Usuario atualizarUsuario(Usuario usuario, String id) {
    Usuario usuarioBanco = buscarUsuarioPorId(id);
    usuarioBanco.setEmail(usuario.getEmail());
    usuario.setSenha(usuario.getSenha());

    usuarioRepository.save(usuarioBanco);
    return usuarioBanco;
  }

  public void deletarusuario(String id) {
    Usuario usuarioBancoDeletado = buscarUsuarioPorId(id);
    usuarioRepository.deleteById(id);
  }

}