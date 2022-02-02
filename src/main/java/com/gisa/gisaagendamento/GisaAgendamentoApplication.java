package com.gisa.gisaagendamento;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class GisaAgendamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GisaAgendamentoApplication.class, args);
	}

}
