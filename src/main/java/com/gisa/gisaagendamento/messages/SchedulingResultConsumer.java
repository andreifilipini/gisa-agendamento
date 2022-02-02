package com.gisa.gisaagendamento.messages;

import com.gisa.gisaagendamento.service.ScheduleService;
import com.gisa.gisacore.dto.BasicTransactionResponseDTO;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class SchedulingResultConsumer {

    @Inject
    private ScheduleService scheduleService;

    @RabbitListener(queues = {"${queue.bookingScheduleResult}"})
    private void receive(@Payload String body) {
        Gson gson = new Gson();
        BasicTransactionResponseDTO response = gson.fromJson(body, BasicTransactionResponseDTO.class);

        if (response.isApproved()) {
            scheduleService.confirm(response.getTransactionId());
        } else {
            scheduleService.cancel(response.getTransactionId());
        }
    }
}
