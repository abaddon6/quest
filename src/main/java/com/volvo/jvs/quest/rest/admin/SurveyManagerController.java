package com.volvo.jvs.quest.rest.admin;

import java.util.Collections;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.jvs.quest.rest.GenericController;

@RestController
@RequestMapping("/admin/survey")
public class SurveyManagerController extends GenericController{

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object addSurvey() {
		return Collections.singletonMap("response", "Survey manager");  
	}
}
