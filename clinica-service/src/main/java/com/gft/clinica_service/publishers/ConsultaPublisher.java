package com.gft.clinica_service.publishers;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.gft.clinica_service.dtos.FinishEvent;

@Component
public class ConsultaPublisher {

    private final RabbitTemplate template;

    public ConsultaPublisher(RabbitTemplate template) {
        this.template = template;
    }

    public void publishConsultaFinalizada(UUID consultaId) {
        FinishEvent event = new FinishEvent(consultaId);

        template.convertAndSend("exchange.consultas", "consulta.finalizada", event);
    }

}
