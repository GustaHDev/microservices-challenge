package com.gft.agendamento_service.publishers;

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

    public void publishProcedimentoCancelado(UUID procedimentoId) {
        Map<String, Object> payload = Map.of("procedimentoId", procedimentoId);

        template.convertAndSend("exchange.procedimentos", "procedimento.cancelado", payload);
    }

}
