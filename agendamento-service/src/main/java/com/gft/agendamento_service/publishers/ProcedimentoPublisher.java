package com.gft.agendamento_service.publishers;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.gft.agendamento_service.dtos.CancelEvent;

@Component
public class ProcedimentoPublisher {

    private final RabbitTemplate template;

    public ProcedimentoPublisher(RabbitTemplate template) {
        this.template = template;
    }

    public void publishProcedimentoCancelado(UUID procedimentoId) {
        CancelEvent event = new CancelEvent(procedimentoId);

        template.convertAndSend("exchange.procedimentos", "procedimento.cancelado", event);
    }

}
