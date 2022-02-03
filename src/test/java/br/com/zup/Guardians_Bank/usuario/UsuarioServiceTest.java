package br.com.zup.Guardians_Bank.usuario;

import br.com.zup.Guardians_Bank.exceptions.UsuarioJaCadastradoException;
import br.com.zup.Guardians_Bank.exceptions.UsuarioNaoEcontradoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class UsuarioServiceTest {

  @MockBean
  private UsuarioRepository usuarioRepository;

  @Autowired
  private UsuarioService usuarioService;

  private Usuario usuario;

  @BeforeEach
  public void setup() {

    usuario = new Usuario();
    usuario.setId("1");
    usuario.setEmail("bqromero@gmail.com");
    usuario.setSenha("xablau123");
  }

  @Test
  public void testarSalvarUsuarioCaminhoPositivo() {
    Mockito.when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
    Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);

    Usuario usuarioRetorno = usuarioService.salvarUsuario(usuario);

    Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuario);
    Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(usuario.getEmail());
  }

  @Test
  public void testarSalvarUsuarioCaminhoNegativo() {
    Mockito.when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

    UsuarioJaCadastradoException excecao = Assertions.assertThrows(UsuarioJaCadastradoException.class,
        () -> usuarioService.salvarUsuario(usuario));

    Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(usuario.getEmail());
  }

  @Test
  public void testarEncontrarUsuarioPorEmailCaminhoPositivo() {
    Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

    Boolean respostaEsperada = usuarioService.encontrarUsuarioPorEmail(Mockito.anyString());
    Assertions.assertTrue(respostaEsperada);

  }

  @Test
  public void testarEncontrarUsuarioPorEmailCaminhoNegativo() {
    Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

    Boolean respostaEsperada = usuarioService.encontrarUsuarioPorEmail(Mockito.anyString());
    Assertions.assertFalse(respostaEsperada);
  }

  @Test
  public void testarBuscarUsuarioPorIdCaminhoPositivo() {
    Mockito.when(usuarioRepository.findById(Mockito.anyString())).thenReturn(Optional.of(usuario));

    Usuario usuarioBanco = usuarioService.buscarUsuarioPorId(usuario.getId());

    Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuario.getId());
  }

  @Test
  public void testarBuscarUsuarioPorIdCaminhoNegativo() {
    Mockito.when(usuarioRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

    UsuarioNaoEcontradoException excecao = Assertions.assertThrows(UsuarioNaoEcontradoException.class,
        () -> usuarioService.buscarUsuarioPorId(usuario.getId()));

    Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuario.getId());
  }

  @Test
  public void testarAtualizarUsuarioCaminhoPositivo() {
    Mockito.when(usuarioRepository.save(Mockito.any())).thenReturn(usuario);
    Mockito.when(usuarioRepository.findById(Mockito.anyString())).thenReturn(Optional.of(usuario));

    usuarioService.atualizarUsuario(usuario, usuario.getId());

    Mockito.verify(usuarioRepository, Mockito.times(1)).save(Mockito.any());
    Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuario.getId());
  }

  @Test
  public void testarAtualizarUsuarioCaminhoNegativo() {
    Mockito.when(usuarioRepository.save(Mockito.any())).thenReturn(usuario);
    Mockito.when(usuarioRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

    UsuarioNaoEcontradoException exception = Assertions.assertThrows(UsuarioNaoEcontradoException.class, () -> {
      usuarioService.atualizarUsuario(usuario, usuario.getId());

      Mockito.verify(usuarioRepository, Mockito.times(1)).save(Mockito.any());
      Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuario.getId());
    });
  }

  @Test
  public void testarDeletarUsuarioCaminhoPositivo() {
    Mockito.when(usuarioRepository.findById(Mockito.anyString())).thenReturn(Optional.of(usuario));
    Mockito.doNothing().when(usuarioRepository).deleteById(Mockito.anyString());

    usuarioService.deletarusuario(usuario.getId());

    Mockito.verify(usuarioRepository, Mockito.times(1)).deleteById(Mockito.anyString());
  }

  @Test
  public void testarDeletarUsuarioCaminhoNegativo() {
    Mockito.when(usuarioRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
    Mockito.doNothing().when(usuarioRepository).deleteById(Mockito.anyString());

    UsuarioNaoEcontradoException exception = Assertions.assertThrows(UsuarioNaoEcontradoException.class, () -> {
      usuarioService.atualizarUsuario(usuario, usuario.getId());

      Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuario.getId());
      Mockito.verify(usuarioRepository, Mockito.times(1)).deleteById(usuario.getId());
    });
  }

}
