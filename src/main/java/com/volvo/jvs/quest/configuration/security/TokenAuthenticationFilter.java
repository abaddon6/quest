package com.volvo.jvs.quest.configuration.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 60_000; // 60 sec.
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    
	public TokenAuthenticationFilter() {
		super();
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "GET"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new GrantedAuthority() {
			private static final long serialVersionUID = -3699590536386576007L;

			@Override
			public String getAuthority() {
				return "Admin";
			}
		});
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				request.getParameter("username"), request.getParameter("token"), authorities);

		// Allow subclasses to set the "details" property
		setDetails(request, token);

		Authentication auth = getAuthenticationManager().authenticate(token);
		if(auth.isAuthenticated()) {
			return auth;
		}
		else {
			throw new AuthenticationException("Failed") {

				private static final long serialVersionUID = 278705174885288226L;
				
			};
		}
	}	
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		setAuthenticationFailureHandler(new AuthenticationFailureHandler() {			
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
			}
		});
		super.unsuccessfulAuthentication(request, response, failed);		
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				String token = Jwts.builder()
		                .setSubject(authResult.getName())
		                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
		                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
		                .compact();
		        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);		        
				response.setStatus(HttpStatus.OK.value());
			}
		});
		super.successfulAuthentication(request, response, chain, authResult);		
	}
}