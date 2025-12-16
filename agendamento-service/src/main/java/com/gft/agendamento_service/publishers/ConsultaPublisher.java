package com.gft.agendamento_service.publishers;

import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsultaPublisher {

    private final RabbitTemplate template;

    public ConsultaPublisher(RabbitTemplate template) {
        this.template = template;
    }

    public void publishConsultaCancelada(UUID consultaId) {
        Map<String, Object> payload = Map.of("consultaId", consultaId);

        template.convertAndSend("exchange.consultas", "consulta.cancelada", payload);
    }

}
