package com.gisa.gisaagendamento.dto;

import com.gisa.gisacore.dto.BasicTransactionRequestDTO;
import lombok.Getter;

@Getter
public class SchedulingRequestDTO extends BasicTransactionRequestDTO {

    private String resourceId;
    private String date;
    private String time;

    public SchedulingRequestDTO(Long transactionId, String resourceId, String date, String time) {
        super(transactionId);
        this.resourceId = resourceId;
        this.date = date;
        this.time = time;
    }
}
