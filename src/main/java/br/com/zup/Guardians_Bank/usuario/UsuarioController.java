package br.com.zup.Guardians_Bank.usuario;

import br.com.zup.Guardians_Bank.config.Security.UsuarioLogado;
import br.com.zup.Guardians_Bank.usuario.dto.CadastroUsuarioDTO;
import br.com.zup.Guardians_Bank.usuario.dto.UsuarioSaidaDTO;
import br.com.zup.Guardians_Bank.usuarioLogado.UsuarioLogadoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
@Api(value = "API para compra de crédito")
@CrossOrigin(origins = "*")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private UsuarioLogadoService usuarioLogadoService;


  @PostMapping
  @ApiOperation(value = "Método responsável por cadastrar um usuário")
  @ResponseStatus(HttpStatus.CREATED)
  public UsuarioSaidaDTO cadastrarUsuario(@RequestBody @Valid CadastroUsuarioDTO cadastroUsuarioDTO) {
    Usuario usuario = modelMapper.map(cadastroUsuarioDTO, Usuario.class);
    usuarioService.salvarUsuario(usuario);
    return modelMapper.map(usuario, UsuarioSaidaDTO.class);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Método responsável por exibir usuários pelo seu ID")
  public UsuarioSaidaDTO exibirUsuario(@PathVariable String id) {
    Usuario usuario = usuarioService.buscarUsuarioPorId(id);
    return modelMapper.map(usuario, UsuarioSaidaDTO.class);

  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Método responsável por atualizar as informações do usuário pelo seu ID")
  public UsuarioSaidaDTO atualizarUsuario(@RequestBody CadastroUsuarioDTO cadastroUsuarioDTO,
                                          Authentication authentication) {

    Usuario usuario = modelMapper.map(cadastroUsuarioDTO, Usuario.class);

    return modelMapper.map(usuarioService.atualizarUsuario(usuario, usuarioLogadoService.pegarId()),
        UsuarioSaidaDTO.class);
  }

  @DeleteMapping({"/{id}"})
  @ApiOperation(value = "Método responsável por deletar um usuário pelo seu ID")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletarUsuario(@PathVariable String id) {
    usuarioService.deletarusuario(id);
  }

}