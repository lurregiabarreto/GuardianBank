package br.com.zup.Guardians_Bank.config.Security;

import br.com.zup.Guardians_Bank.config.JWT.FiltroDeAutenticacaoJWT;
import br.com.zup.Guardians_Bank.config.JWT.JWTComponent;
import br.com.zup.Guardians_Bank.config.JWT.FiltroDeAutorizacaoJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ConfiguracaoDeSeguranca extends WebSecurityConfigurerAdapter {

  @Autowired
  private JWTComponent jwtComponent;
  @Autowired
  private UserDetailsService detailsService;

  private static final String[] ENDPOINT_POST_PUBLICO = {
      "/usuario"
  };

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.cors().configurationSource(configurarCORS());

    http.authorizeRequests()
        .antMatchers(HttpMethod.POST, ENDPOINT_POST_PUBLICO).permitAll()
        .anyRequest().authenticated();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilter(new FiltroDeAutenticacaoJWT(jwtComponent, authenticationManager()));
    http.addFilter(new FiltroDeAutorizacaoJWT(authenticationManager(), jwtComponent, detailsService));

  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(detailsService).passwordEncoder(bCryptPasswordEncoder());
  }

  @Bean
  CorsConfigurationSource configurarCORS() {
    UrlBasedCorsConfigurationSource cors = new UrlBasedCorsConfigurationSource();
    cors.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return cors;
  }

  @Bean
  BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}