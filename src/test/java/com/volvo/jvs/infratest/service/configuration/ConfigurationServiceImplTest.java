package com.volvo.jvs.infratest.service.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.volvo.jvs.quest.service.configuration.ConfigurationService;
import com.volvo.jvs.quest.service.configuration.ConfigurationServiceImpl;

@RunWith(SpringRunner.class)
public class ConfigurationServiceImplTest {

	@Mock
	private CorsRegistry corsRegisrty;
	
	@InjectMocks
	private ConfigurationService configurationService = new ConfigurationServiceImpl();
	
	@Test
	public void addCorsMappings() {
		configurationService.addCorsMappings(corsRegisrty);
		
		Mockito.verify(corsRegisrty).addMapping("/**");
	}
}
