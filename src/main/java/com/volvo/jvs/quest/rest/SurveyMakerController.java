package com.volvo.jvs.quest.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.jvs.quest.database.entity.Survey;
import com.volvo.jvs.quest.database.repository.SurveyRepository;

@RestController
@RequestMapping("/survey")
public class SurveyMakerController extends GenericController{

	@Autowired
	private SurveyRepository surveyRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.CREATED)
	public Survey addSurvey() {
		Survey surveyToSave = new Survey();
		surveyToSave.setSurveyId("1234567890");
		surveyToSave.setName("DEV-OPS Self Assesment");
		getSurveyRepository().save(surveyToSave);
		
		Survey foundSurvey = null;
		for(Survey survey : getSurveyRepository().findAll()) {
			foundSurvey = survey;
		}
		
		foundSurvey.add(linkTo(methodOn(SurveyMakerController.class).addSurvey()).withSelfRel());

		return foundSurvey;
	}
	
	protected SurveyRepository getSurveyRepository() {
		return surveyRepository;
	}
}
