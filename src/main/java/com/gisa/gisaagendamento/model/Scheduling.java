package com.gisa.gisaagendamento.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Scheduling {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String associateLogin;

	@NotNull
	private String resourceId;

	@NotNull
	private String date;

	@NotNull
	private String time;

	@NotNull
	private SchedulingStatus status;

	public Scheduling(String associateLogin, String resourceId, String date, String time) {
		super();
		this.associateLogin = associateLogin;
		this.resourceId = resourceId;
		this.date = date;
		this.time = time;
		this.status = SchedulingStatus.PENDING;
	}

	public void cancel() {
		this.status = SchedulingStatus.CANCELED;
	}

	public void confirm() {
		this.status = SchedulingStatus.CONFIRMED;
	}
}
