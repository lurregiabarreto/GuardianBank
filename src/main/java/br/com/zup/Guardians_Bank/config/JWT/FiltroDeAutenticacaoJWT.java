package br.com.zup.Guardians_Bank.config.JWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class FiltroDeAutenticacaoJWT extends UsernamePasswordAuthenticationFilter {
    private JWTComponent jwtComponent;
    private AuthenticationManager authenticationManager;

    public FiltroDeAutenticacaoJWT(JWTComponent jwtComponent, AuthenticationManager authenticationManager) {
        this.jwtComponent = jwtComponent;
        this.authenticationManager = authenticationManager;
    }
}