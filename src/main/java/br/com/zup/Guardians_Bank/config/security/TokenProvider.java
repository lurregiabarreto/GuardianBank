package br.com.zup.Guardians_Bank.config.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Component
public class TokenProvider implements Serializable {

    @Value("${jwt.segredo}")
    private String segredo;
    @Value("${jwt.milissegundos}")
    private Long milissegundo;
    @Value("${jwt.autoridad}")
    private String autoridad;

    public String pegarUsuarioToken(String token) {
        return pegarClaimPorToken(token, Claims::getSubject);
    }

    public Date pegarValidacaoToken(String token) {
        return pegarClaimPorToken(token, Claims::getExpiration);
    }

    public <T> T pegarClaimPorToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(segredo)
                .parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = pegarValidacaoToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(autoridad, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + milissegundo * 1000))
                .signWith(SignatureAlgorithm.HS256, segredo)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = pegarUsuarioToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    UsernamePasswordAuthenticationToken pegarAutenticacaoToken(final String token, final Authentication existingAuth, final UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(segredo);
        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(autoridad).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }


}
