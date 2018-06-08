package com.volvo.jvs.quest.database.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.volvo.jvs.quest.util.collection.Orderable;

@Entity
public class Question implements Orderable{

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer questionId;
	
	@Enumerated(EnumType.STRING)
	@Size(min=1, max=20)
	@NotNull
	private QuestionType questionType = QuestionType.MULTIPLE_CHOICE;
	
	@Size(min=1, max=1000)
	@NotNull
	private String content;

	@NotNull
	private Integer orderNumber;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="questionId", nullable = false)
	@OrderBy("orderNumber ASC")
	private Set<Answer> answers = new LinkedHashSet<>();

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public Integer getOrderNumber() {
		return orderNumber;
	}

	@Override
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Set<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<Answer> answers) {
		this.answers = answers;
	}
}
