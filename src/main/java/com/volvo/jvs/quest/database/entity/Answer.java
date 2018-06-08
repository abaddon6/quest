package com.volvo.jvs.quest.database.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.volvo.jvs.quest.rest.View;
import com.volvo.jvs.quest.util.collection.Orderable;

@Entity
public class Answer implements Orderable{

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer answerId;
	
	@Size(min=1, max=1000)
	private String content;

	@JsonView(View.ManagementView.class)
	private Integer score;
	
	@NotNull
	@JsonView(View.ManagementView.class)
	private Integer orderNumber;

	@Transient
	@JsonView(View.DetailedView.class)
	private Response response;
	
	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public Integer getOrderNumber() {
		return orderNumber;
	}

	@Override
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
}
