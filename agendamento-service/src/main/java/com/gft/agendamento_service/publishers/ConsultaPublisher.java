package com.gft.agendamento_service.publishers;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.gft.agendamento_service.dtos.CancelEvent;

@Component
public class ConsultaPublisher {

    private final RabbitTemplate template;

    public ConsultaPublisher(RabbitTemplate template) {
        this.template = template;
    }

    public void publishConsultaCancelada(UUID consultaId) {
        CancelEvent event = new CancelEvent(consultaId);

        template.convertAndSend("exchange.consultas", "consulta.cancelada", event);
    }

}
