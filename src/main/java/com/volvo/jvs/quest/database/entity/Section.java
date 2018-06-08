package com.volvo.jvs.quest.database.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
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

import com.fasterxml.jackson.annotation.JsonView;
import com.volvo.jvs.quest.rest.View;
import com.volvo.jvs.quest.util.collection.Orderable;

@Entity
public class Section implements Orderable{

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer sectionId;
	
	@Size(min=1, max=100)
	@NotNull
	private String title;		

	@NotNull
	private Integer orderNumber;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="sectionId", nullable = false)
	@OrderBy("orderNumber ASC")
	@JsonView(View.ManagementView.class)
	private Set<Question> questions = new LinkedHashSet<>();
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="sectionId", nullable = false)
	@OrderBy("percentageLevel ASC ")
	@JsonView(value={View.ReportView.class, View.ManagementView.class})
	private Set<SectionMaturity> maturities = new LinkedHashSet<>();
	
	@Convert(converter=SectionOptionConverter.class)
	@JsonView(value={View.ReportView.class, View.ManagementView.class})
	private SectionOption options;
	
	@Transient
	@JsonView(View.ReportView.class)
	private Integer maturityNumber;
	
	@Transient
	@JsonView(View.DetailedView.class)
	private Integer numberOfResponses;
	
	@Transient
	@JsonView(View.DetailedView.class)
	public Integer getNumberOfQuestions() {
		return questions.size();
	}
	
	@Transient
	@JsonView(View.ReportView.class)
	private Integer maxScore;
	
	@Transient
	@JsonView(View.ReportView.class)
	private Integer score;
	
	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Integer getOrderNumber() {
		return orderNumber;
	}

	@Override
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public Integer getNumberOfResponses() {
		return numberOfResponses;
	}

	public void setNumberOfResponses(Integer numberOfResponses) {
		this.numberOfResponses = numberOfResponses;
	}

	public Set<SectionMaturity> getMaturities() {
		return maturities;
	}

	public void setMaturities(Set<SectionMaturity> maturities) {
		this.maturities = maturities;
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

	public Integer getMaturityNumber() {
		return maturityNumber;
	}

	public void setMaturityNumber(Integer maturityNumber) {
		this.maturityNumber = maturityNumber;
	}

	public SectionOption getOptions() {
		return options;
	}

	public void setOptions(SectionOption options) {
		this.options = options;
	}
}