package com.volvo.jvs.quest.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.volvo.jvs.quest.database.entity.Survey;
import com.volvo.jvs.quest.database.repository.SurveyRepository;
import com.volvo.jvs.quest.util.rest.RestUtil;

@RestController
@RequestMapping(value="/surveys",
                produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
public class SurveyController extends GenericController{

	@Autowired	
	private SurveyRepository surveyRepository;
	
	@Autowired
	private RestUtil restUtil;

	@GetMapping
	@JsonView(View.ListView.class)
	public List<Resource<Survey>> getAllSurveys() {
		return getRestUtil().getResourceList(getSurveyRepository().findAll(true));
	}
	
	@GetMapping("/{surveyId}")
	@JsonView(View.ManagementView.class)
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Resource<Survey> getSurvey(@PathVariable("surveyId") Integer surveyId) {
		return getRestUtil().getResource(getSurveyRepository().findOne(surveyId));
	}

	protected SurveyRepository getSurveyRepository() {
		return surveyRepository;
	}

	protected RestUtil getRestUtil() {
		return restUtil;
	}
}
