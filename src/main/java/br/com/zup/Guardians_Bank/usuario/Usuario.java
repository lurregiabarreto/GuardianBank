package br.com.zup.Guardians_Bank.usuario;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String senha;

}
