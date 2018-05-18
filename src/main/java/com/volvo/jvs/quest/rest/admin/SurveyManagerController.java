package com.volvo.jvs.quest.rest.admin;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.jvs.quest.database.entity.Survey;
import com.volvo.jvs.quest.database.repository.SurveyRepository;
import com.volvo.jvs.quest.rest.GenericController;

@RestController
@RequestMapping("/admin/survey")
public class SurveyManagerController extends GenericController{

	@Autowired
	private SurveyRepository surveyRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object addSurvey() {
		StringBuilder surveys = new StringBuilder();
		for(Survey survey : getSurveyRepository().findAll()) {
			surveys.append(survey.getSurveyId()).append(" - ").append(survey.getName()).append("; ");
		}
		return Collections.singletonMap("response", "Administrator surveys: " + surveys.toString());  
	}

	protected SurveyRepository getSurveyRepository() {
		return surveyRepository;
	}
}
