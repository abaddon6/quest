package com.volvo.jvs.quest.service.configuration;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.volvo.jvs.quest.configuration.QuestConfiguration;
import com.volvo.jvs.quest.database.entity.Survey;
import com.volvo.jvs.quest.database.repository.SurveyRepository;

@DataJpaTest
@RunWith(SpringRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@ActiveProfiles("junit")
public class ConfigurationServiceImplTest {

    @Autowired
    private TestEntityManager entityManager;

	@Autowired
	private SurveyRepository surveyRepository;
	
	@Mock
	private CorsRegistry corsRegisrty;
	
	@InjectMocks
	private QuestConfiguration questConfiguration = new QuestConfiguration();
	
	@Test
	@DatabaseSetup("sampleData.xml")
	@ExpectedDatabase(value="expectedData.xml", assertionMode=DatabaseAssertionMode.NON_STRICT)
	public void databaseTest() {
		List<Survey> surveys = new ArrayList<>();
		for(Survey survey : surveyRepository.findAll()) {
			surveys.add(survey);
		}
		assertEquals(1, surveys.size());
	}	
}
