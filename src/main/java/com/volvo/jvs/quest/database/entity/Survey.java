package com.volvo.jvs.quest.database.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.Resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.volvo.jvs.quest.rest.View;

@Entity
public class Survey {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer surveyId;
		
	@Size(min=1, max=100)
	@NotNull
	private String name;
	
	@Size(min=0, max=1000)
	private String description;
	
	@NotNull
	@JsonView(View.ManagementView.class)
	private Boolean publish = Boolean.FALSE;

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="surveyId", nullable = false)
	@OrderBy("orderNumber ASC")
	@JsonView(value={View.DetailedView.class, View.ReportView.class})
	private Set<Section> sections = new LinkedHashSet<>();

	@Transient
	@JsonView(value={View.DetailedView.class, View.ReportView.class})
	private Integer numberOfResponses;
	
	@Transient
	@JsonView(View.DetailedView.class)
	private Section respondingSection;
	
	@Transient
	@JsonView(View.DetailedView.class)
	private Resource<Question> respondingQuestion;
	
	@Transient
	@JsonView(value={View.DetailedView.class, View.ReportView.class})
	public Integer getNumberOfQuestions() {
		int counter = 0;
		if(sections != null) {
			for(Section section : sections) {
				counter += section.getQuestions().size();
			} 
		}
		return counter;
	}
	
	@Transient
	@JsonView(View.ReportView.class)
	private Integer maxScore;
	
	@Transient
	@JsonView(View.ReportView.class)
	private Integer score;

	public Survey() {
		super();
	}

	public Survey(Integer surveyId, String name, String description) {
		super();
		this.surveyId = surveyId;
		this.name = name;
		this.description = description;
	}
	
	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
	}

	public Set<Section> getSections() {
		return sections;
	}

	public void setSections(Set<Section> sections) {		
		this.sections = sections;
	}

	public Integer getNumberOfResponses() {
		return numberOfResponses;
	}

	public void setNumberOfResponses(Integer numberOfResponses) {
		this.numberOfResponses = numberOfResponses;
	}

	public Section getRespondingSection() {
		return respondingSection;
	}

	public void setRespondingSection(Section respondingSection) {
		this.respondingSection = respondingSection;
	}

	public Resource<Question> getRespondingQuestion() {
		return respondingQuestion;
	}

	public void setRespondingQuestion(Resource<Question> respondingQuestion) {
		this.respondingQuestion = respondingQuestion;
	}

	public Integer getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
}
