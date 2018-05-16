package com.volvo.jvs.quest.configuration;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvo.jvs.quest.configuration.security.CustomAuthenticationProvider;
import com.volvo.jvs.quest.configuration.security.JWTAuthorizationFilter;
import com.volvo.jvs.quest.configuration.security.TokenAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class QuestConfiguration {

	@Bean
	public WebSecurityConfigurer<WebSecurity> webSecurityConfigurer() {
		return new WebSecurityConfigurerAdapter() {
			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http
					.csrf()
						.disable()
					.httpBasic()
						.disable()	
					.sessionManagement()
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()						
					.cors().and()
		            .exceptionHandling()
		            	.authenticationEntryPoint(new Http401AuthenticationEntryPoint("401")).and()
		            .logout()
		            	.logoutUrl("/logout")
		            	.logoutSuccessHandler(this::logoutSuccessHandler)
		            	.invalidateHttpSession(true)   
		            	.deleteCookies("JSESSIONID").and()
					.authorizeRequests().antMatchers("/admin/**").authenticated().and()
					.authorizeRequests().antMatchers("/**").permitAll().and()
					//.addFilterBefore(tokenFilter(), BasicAuthenticationFilter.class)
					.addFilter(jWTAuthorizationFilter());
			}
			
			private void logoutSuccessHandler(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException {
				response.setStatus(HttpStatus.OK.value());
				new ObjectMapper().writeValue(response.getWriter(), "Bye!");
			}
		};
	}
	
	@Bean
	public JWTAuthorizationFilter jWTAuthorizationFilter() {
		return new JWTAuthorizationFilter(new ProviderManager(Arrays.asList(authProvider())));
	}
	
	@Bean
	public TokenAuthenticationFilter tokenFilter() {
		TokenAuthenticationFilter tokenFilter = new TokenAuthenticationFilter();
		tokenFilter.setAuthenticationManager(new ProviderManager(Arrays.asList(authProvider())));
		return tokenFilter;
	}

	@Bean
	public CustomAuthenticationProvider authProvider() {
		return new CustomAuthenticationProvider();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
    public WebMvcConfigurer webMvcConfigurer() {		
		return new WebMvcConfigurerAdapter() {		
			@Bean
			MethodValidationPostProcessor methodValidationPostProcessor() {
			      return new MethodValidationPostProcessor();
			}
		};		
	}
}