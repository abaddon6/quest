package com.volvo.jvs.quest.database.entity;

import javax.validation.constraints.NotNull;

public class SectionOption {

	@NotNull
	private Boolean onRadar;

	public Boolean getOnRadar() {
		return onRadar;
	}

	public void setOnRadar(Boolean onRadar) {
		this.onRadar = onRadar;
	}
}
