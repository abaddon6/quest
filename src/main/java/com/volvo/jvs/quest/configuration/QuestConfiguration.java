package com.volvo.jvs.quest.configuration;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.volvo.jvs.quest.configuration.security.JWTAuthenticationFilter;
import com.volvo.jvs.quest.configuration.security.JWTAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
					.addFilter(jWTAuthenticationFilter());
			}
		};
	}
	
	@Bean
	public JWTAuthenticationFilter jWTAuthenticationFilter() {
		return new JWTAuthenticationFilter(new ProviderManager(Arrays.asList(authProvider())));
	}

	@Bean
	public JWTAuthenticationProvider authProvider() {
		return new JWTAuthenticationProvider();
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