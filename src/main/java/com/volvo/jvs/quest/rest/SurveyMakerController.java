package com.volvo.jvs.quest.rest;

import java.util.Collections;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/survey")
public class SurveyMakerController extends GenericController{

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object addSurvey() {
		return Collections.singletonMap("response", "Survey maker"); 
	}
}
