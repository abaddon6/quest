package com.volvo.jvs.quest.configuration.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication arg0) throws AuthenticationException {
		if(arg0.getCredentials() != null && arg0.getCredentials().equals("token")) {
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new GrantedAuthority() {
				private static final long serialVersionUID = -3699590536386576007L;

				@Override
				public String getAuthority() {
					return "Admin";
				}
			});
			return new UsernamePasswordAuthenticationToken(
		             arg0.getCredentials(), arg0.getCredentials(), authorities);
		}
		return new UsernamePasswordAuthenticationToken(arg0.getPrincipal(), arg0.getCredentials());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
	

}