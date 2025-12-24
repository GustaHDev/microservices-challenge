package com.gft.agendamento_service.consumers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gft.agendamento_service.dtos.FinishEvent;
import com.gft.agendamento_service.exceptions.ResourceNotFoundException;
import com.gft.agendamento_service.services.ProcedimentoService;

@Component
public class ProcedimentoConsumer {

    public static final Logger log = LoggerFactory.getLogger(ProcedimentoConsumer.class);
    private final ProcedimentoService procedimentoService;

    public ProcedimentoConsumer(ProcedimentoService procedimentoService) {
        this.procedimentoService = procedimentoService;
    }

    @RabbitListener(queues = "procedimento.finalizado.queue")
    public void onProcedimentoFinalizado(FinishEvent event) {
        try {
            this.procedimentoService.finalizarProcedimento(event.getId());
            log.info("Consulta {} finalizada com sucesso via mensageria.", event.getId());
        } catch (ResourceNotFoundException e) {
            log.warn("Recebi evento de finalização, mas a consulta {} não existe no banco de agendamento. Mensagem descartada.", event.getId());
        } catch (Exception e) {
            log.error("Erro desconhecido ao processar mensagem: ", e);
            throw e;
        }
    }

}
