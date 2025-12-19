package com.gft.procedimento_service.publishers;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.gft.procedimento_service.dtos.FinishEvent;

@Component
public class ProcedimentoPublisher {

    private final RabbitTemplate template;

    public ProcedimentoPublisher(RabbitTemplate template) {
        this.template = template;
    }

    public void publishProcedimentoFinalizado(UUID procedimentoId) {
        FinishEvent event = new FinishEvent(procedimentoId);

        this.template.convertAndSend("exchange.procedimentos", "procedimento.finalizado", event);
    }

}
