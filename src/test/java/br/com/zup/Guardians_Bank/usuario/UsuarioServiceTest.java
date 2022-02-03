package br.com.zup.Guardians_Bank.usuario;

import br.com.zup.Guardians_Bank.exceptions.UsuarioJaCadastradoException;
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
  public void testarBuscarUsarioPorIdCaminhoPositivo() {
    Mockito.when(usuarioRepository.findById(Mockito.anyString())).thenReturn(Optional.of(usuario));

    Usuario usuarioBanco = usuarioService.buscarUsuarioPorId(usuario.getId());

    Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuario.getId());
  }

}
