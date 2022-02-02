package com.gisa.gisaagendamento.messages;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component
public class QueueConfig {

    @Inject
    private AmqpAdmin amqpAdmin;

    @Value("${queue.bookingSchedule}")
    private String bookingScheduleQueueName;

    @Value("${queue.bookingScheduleResult}")
    private String bookingScheduleResultQueueName;

    @Value("${queue.cancelSchedule}")
    private String cancelScheduleQueueName;

    @PostConstruct
    protected void createQueues() {
        amqpAdmin.declareQueue(new Queue(this.bookingScheduleQueueName, true));
        amqpAdmin.declareQueue(new Queue(this.bookingScheduleResultQueueName, true));
        amqpAdmin.declareQueue(new Queue(this.cancelScheduleQueueName, true));
    }
}
