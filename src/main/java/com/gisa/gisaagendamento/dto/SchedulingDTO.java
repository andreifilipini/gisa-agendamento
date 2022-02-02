package com.gisa.gisaagendamento.dto;

import com.gisa.gisaagendamento.model.SchedulingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SchedulingDTO {

	private String resource;

	private String date;

	private String time;

	private SchedulingStatus status;
}
