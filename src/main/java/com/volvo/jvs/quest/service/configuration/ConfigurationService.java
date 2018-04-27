package com.volvo.jvs.quest.service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public interface ConfigurationService {

    @Bean
	public MethodValidationPostProcessor methodValidationPostProcessor();
    
    public void addCorsMappings(CorsRegistry registry);
	 
	public String getServerTimeZone();
}
