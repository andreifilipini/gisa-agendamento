package com.gisa.gisaagendamento.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Scheduling {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String associateId;

	@NotNull
	private String resourceId;

	@NotNull
	private String date;

	@NotNull
	private String time;

	@NotNull
	@Enumerated(EnumType.STRING)
	private SchedulingStatus status;

	public Scheduling(String associateId, String resourceId, String date, String time) {
		super();
		this.associateId = associateId;
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
