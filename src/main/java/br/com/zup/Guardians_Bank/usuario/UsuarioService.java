package br.com.zup.Guardians_Bank.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;
  @Autowired
  private BCryptPasswordEncoder encoder;

  public Usuario salvarUsuario(Usuario usuario) {
    String senhaEscondida = encoder.encode(usuario.getSenha());

    usuario.setSenha(senhaEscondida);
    return usuarioRepository.save(usuario);
  }

  public boolean encontrarUsuarioPorEmail(String email) {
    Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
    if (!usuarioOptional.isEmpty()) {
      return true;
    } else {
      return false;
    }

  }

  public void atualizarUsuario(Usuario usuario, String id) {
    Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

    if (usuarioOptional.isEmpty()) {
      throw new RuntimeException("Usuario não existe");
    }

    Usuario usuarioBanco = usuarioOptional.get();
    if (!usuarioBanco.getEmail().equals(usuario.getEmail())) {
      usuarioBanco.setEmail(usuario.getEmail());
    }

    String senhaEscondida = encoder.encode(usuario.getSenha());
    usuarioBanco.setSenha(senhaEscondida);
    usuarioRepository.save(usuarioBanco);
  }

}