package com.volvo.jvs.quest.configuration.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {

	@Value("${spring.security.token-prefix}")
	public String tokenPrefix;
	
    @Value("${spring.security.secret}")
    private String secret;
    
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String token = (String) authentication.getCredentials();
		if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(getSecret().getBytes())
                    .parseClaimsJws(token.replace(getTokenPrefix(), ""))
                    .getBody()
                    .getSubject();
           
            if (user != null) {
            	List<GrantedAuthority> authorities = new ArrayList<>();
        		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN") );        		
            	return new UsernamePasswordAuthenticationToken(user, token, authorities);
            }           
        }	
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	protected String getSecret() {
		return secret;
	}

	protected String getTokenPrefix() {
		return tokenPrefix;
	}
}