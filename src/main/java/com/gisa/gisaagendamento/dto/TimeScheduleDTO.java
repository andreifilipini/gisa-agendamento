package com.gisa.gisaagendamento.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimeScheduleDTO {

    private String time;

    private boolean available;
}
