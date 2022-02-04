package com.gisa.gisaagendamento.messages;

import com.gisa.gisaagendamento.service.ScheduleService;
import com.gisa.gisacore.dto.BasicTransactionResponseDTO;
import com.gisa.gisacore.messages.AbstractRabbitConsumer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Slf4j
@Component
public class SchedulingResultConsumer extends AbstractRabbitConsumer {

    @Inject
    private ScheduleService scheduleService;

    @RabbitListener(queues = {"${queue.bookingScheduleResult}"})
    protected void receive(@Payload String body) {
        executeLoggin(body);
    }

    @Override
    protected void execute(@Payload String body) {
        Gson gson = new Gson();
        BasicTransactionResponseDTO response = gson.fromJson(body, BasicTransactionResponseDTO.class);

        if (response.isApproved()) {
            scheduleService.confirm(response.getTransactionId());
        } else {
            scheduleService.cancel(response.getTransactionId());
        }
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
