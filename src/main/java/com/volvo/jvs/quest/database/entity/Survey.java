package com.volvo.jvs.quest.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.hateoas.ResourceSupport;

@Entity(name="survey")
public class Survey extends ResourceSupport {

	@Id
	@Column(name="surveyid")
	private String surveyId;
		
	@Column(name="name")
	private String name;

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
