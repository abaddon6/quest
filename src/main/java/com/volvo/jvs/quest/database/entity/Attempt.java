package com.volvo.jvs.quest.database.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.volvo.jvs.quest.rest.View;

@Entity
public class Attempt {

	@Id
	@Size(min=1, max=100)
	@NotNull
	private String attemptId;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="surveyId")
	private Survey survey;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="userGroupId")
	private UserGroup userGroup;
	
	@NotNull
	@JsonView(View.ManagementView.class)
	private Integer attemptNumber;
	
	@Convert(converter=ResponseConverter.class)
	@JsonView(View.ManagementView.class)
	private List<Response> responses = new ArrayList<>();

	public String getAttemptId() {
		return attemptId;
	}

	public void setAttemptId(String attemptId) {
		this.attemptId = attemptId;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public List<Response> getResponses() {
		return responses;
	}

	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}

	public Integer getAttemptNumber() {
		return attemptNumber;
	}

	public void setAttemptNumber(Integer attemptNumber) {
		this.attemptNumber = attemptNumber;
	}
}