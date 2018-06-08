package com.volvo.jvs.quest.database.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class SectionMaturity {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer sectionMaturityId;
	
	@Size(min=0, max=1000)
	private String content;

	@NotNull
	private Integer percentageLevel;

	public Integer getSectionMaturityId() {
		return sectionMaturityId;
	}

	public void setSectionMaturityId(Integer sectionMaturityId) {
		this.sectionMaturityId = sectionMaturityId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getPercentageLevel() {
		return percentageLevel;
	}

	public void setPercentageLevel(Integer percentageLevel) {
		this.percentageLevel = percentageLevel;
	}
}
