package br.com.zup.Guardians_Bank.usuario;

import br.com.zup.Guardians_Bank.components.Conversor;
import br.com.zup.Guardians_Bank.config.JWT.JWTComponent;
import br.com.zup.Guardians_Bank.config.Security.UsuarioLoginService;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamento;
import br.com.zup.Guardians_Bank.proposta.PropostaController;
import br.com.zup.Guardians_Bank.usuario.dto.CadastroUsuarioDTO;
import br.com.zup.Guardians_Bank.usuario.dto.UsuarioSaidaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest({UsuarioController.class, Conversor.class, UsuarioLoginService.class, JWTComponent.class})
public class UsuarioControllerTest {

  @MockBean
  private UsuarioService usuarioService;
  @MockBean
  private UsuarioLoginService usuarioLoginService;
  @MockBean
  private JWTComponent jwtComponent;

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private Usuario usuario;
  private CadastroUsuarioDTO cadastroUsuarioDTO;
  private UsuarioSaidaDTO usuarioSaidaDTO;

  @BeforeEach
  public void setup() {
    objectMapper = new ObjectMapper();

    usuario = new Usuario();
    usuario.setId("1");
    usuario.setSenha("1234");
    usuario.setEmail("bqromero@gmail.com");

    cadastroUsuarioDTO = new CadastroUsuarioDTO();
    cadastroUsuarioDTO.setEmail("dorayen@zup.com");
    cadastroUsuarioDTO.setSenha("369@");

    usuarioSaidaDTO = new UsuarioSaidaDTO();
    usuarioSaidaDTO.setEmail("lurregia@bol.com.br");
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarCadastroDeUsuario() throws Exception {

    Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
    String json = objectMapper.writeValueAsString(cadastroUsuarioDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/usuario")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect((MockMvcResultMatchers.status().is(201)));

    String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    UsuarioSaidaDTO usuarioResposta = objectMapper.readValue(jsonResposta, UsuarioSaidaDTO.class);
  }

  @Test
  @WithMockUser("user@user.com")
  public void testarCadastrarComValidacaoEmailBlank() throws Exception {
    cadastroUsuarioDTO.setEmail("    ");
    Mockito.when((usuarioService.salvarUsuario(Mockito.any(Usuario.class)))).thenReturn(usuario);
    String json = objectMapper.writeValueAsString(cadastroUsuarioDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/usuario")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

}
