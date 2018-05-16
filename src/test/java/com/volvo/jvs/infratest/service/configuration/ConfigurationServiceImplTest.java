package com.volvo.jvs.infratest.service.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.volvo.jvs.quest.configuration.QuestConfiguration;

@RunWith(SpringRunner.class)
public class ConfigurationServiceImplTest {

	@Mock
	private CorsRegistry corsRegisrty;
	
	@InjectMocks
	private QuestConfiguration questConfiguration = new QuestConfiguration();
	
	@Test
	public void webSecurityConfigurer() {
		
	}	
}
