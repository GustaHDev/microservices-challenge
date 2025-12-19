package com.gft.agendamento_service.consumers;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gft.agendamento_service.dtos.FinishEvent;
import com.gft.agendamento_service.services.ProcedimentoService;

@Component
public class ProcedimentoConsumer {

    private final ProcedimentoService procedimentoService;

    public ProcedimentoConsumer(ProcedimentoService procedimentoService) {
        this.procedimentoService = procedimentoService;
    }

    @RabbitListener(queues = "procedimento.finalizado.queue")
    public void onProcedimentoFinalizado(FinishEvent event) {
        this.procedimentoService.finalizarProcedimento(event.getId());
    }

}
