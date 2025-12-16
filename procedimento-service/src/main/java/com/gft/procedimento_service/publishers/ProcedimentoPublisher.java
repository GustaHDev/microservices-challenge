package com.gft.procedimento_service.publishers;

import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProcedimentoPublisher {

    private final RabbitTemplate template;

    public ProcedimentoPublisher(RabbitTemplate template) {
        this.template = template;
    }

    public void publishProcedimentoFinalizado(UUID procedimentoId) {
        Map<String, Object> payload = Map.of("procedimentoId", procedimentoId);

        this.template.convertAndSend("exchange.procedimentos", "procedimento.finalizado", payload);
    }

}
