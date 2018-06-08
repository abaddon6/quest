package com.volvo.jvs.quest.configuration.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	@Value("${spring.security.token-prefix}")
	public String tokenPrefix;
	
	@Value("${spring.security.header-string}")
	public String headerString;
	
	public JWTAuthenticationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
    	String token = req.getHeader(getHeaderString());

        if (token == null || !token.startsWith(getTokenPrefix())) {
            chain.doFilter(req, res);
            return;
        }
        else {
        	SecurityContextHolder.getContext().setAuthentication(
        			getAuthenticationManager().authenticate(
        					new UsernamePasswordAuthenticationToken(null, token)));
        	chain.doFilter(req, res);
        }
    }

	protected String getTokenPrefix() {
		return tokenPrefix;
	}

	protected String getHeaderString() {
		return headerString;
	}
}
