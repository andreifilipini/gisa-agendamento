package com.gisa.gisaagendamento.messages;

import com.gisa.gisaagendamento.dto.SchedulingRequestDTO;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CancelSchedulingProducer {

    @Inject
    private RabbitTemplate rabbitTemplate;

    @Value("${queue.cancelSchedule}")
    private String cancelScheduleQueueName;

    public void send(Long transactionId, String resourceId, String date, String time) {
        Gson gson = new Gson();
        SchedulingRequestDTO request = new SchedulingRequestDTO(transactionId, resourceId, date, time);
        rabbitTemplate.convertAndSend(this.cancelScheduleQueueName, gson.toJson(request));
    }
}
