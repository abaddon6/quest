package com.volvo.jvs.quest.service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Service
public class ConfigurationServiceImpl extends WebMvcConfigurerAdapter implements ConfigurationService {

	@Autowired
	private ConfigurationData configurationData;
	
    @Override
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	      return new MethodValidationPostProcessor();
	}
    
    @Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	
    @Override
	public String getServerTimeZone() {
		return configurationData.getServerTimeZone();
	}
}
