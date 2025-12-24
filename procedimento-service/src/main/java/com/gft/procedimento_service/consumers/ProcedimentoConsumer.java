package com.gft.procedimento_service.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gft.procedimento_service.dtos.CancelEvent;
import com.gft.procedimento_service.services.ProcedimentoService;

@Component
public class ProcedimentoConsumer {

    private final ProcedimentoService procedimentoService;

    public ProcedimentoConsumer(ProcedimentoService procedimentoService) {
        this.procedimentoService = procedimentoService;
    }

    @RabbitListener(queues = "procedimento.cancelado.queue")
    public void onProcedimentoCancelado(CancelEvent event) {
        this.procedimentoService.cancelarProcedimento(event.getId());
    }

}
