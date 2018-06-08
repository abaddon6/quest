package com.volvo.jvs.quest.database.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Response {

	@NotNull
	private Integer answerId;
	
	@Size(min=0, max=1000)
	@NotNull
	private String content;

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
}
