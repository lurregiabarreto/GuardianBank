package br.com.zup.Guardians_Bank.usuario;

import br.com.zup.Guardians_Bank.config.Security.UsuarioLogado;
import br.com.zup.Guardians_Bank.usuario.dto.CadastroUsuarioDTO;
import br.com.zup.Guardians_Bank.usuario.dto.UsuarioSaidaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;
  @Autowired
  private ModelMapper modelMapper;


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UsuarioSaidaDTO cadastrarUsuario(@RequestBody @Valid CadastroUsuarioDTO cadastroUsuarioDTO) {
    Usuario usuario = modelMapper.map(cadastroUsuarioDTO, Usuario.class);
    usuarioService.salvarUsuario(usuario);
    return modelMapper.map(usuario, UsuarioSaidaDTO.class);
  }

  @GetMapping("/{id}")
  public UsuarioSaidaDTO exibirUsuario(@PathVariable String id) {
    Usuario usuario = usuarioService.buscarUsuarioPorId(id);
    return modelMapper.map(usuario, UsuarioSaidaDTO.class);

  }

  @PutMapping("/{id}")
  public UsuarioSaidaDTO atualizarUsuario(@RequestBody CadastroUsuarioDTO cadastroUsuarioDTO,
                                          Authentication authentication) {

    UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
    Usuario usuario = modelMapper.map(cadastroUsuarioDTO, Usuario.class);

    return modelMapper.map(usuarioService.atualizarUsuario(usuario, usuarioLogado.getId()), UsuarioSaidaDTO.class);
  }

  @DeleteMapping({"/{id}"})
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletarUsuario(@PathVariable String id) {
    usuarioService.deletarusuario(id);
  }

}