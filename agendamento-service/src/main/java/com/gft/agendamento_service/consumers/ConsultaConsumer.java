package com.gft.agendamento_service.consumers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gft.agendamento_service.dtos.FinishEvent;
import com.gft.agendamento_service.exceptions.ResourceNotFoundException;
import com.gft.agendamento_service.services.ConsultaService;

@Component
public class ConsultaConsumer {

    public static final Logger log = LoggerFactory.getLogger(ConsultaConsumer.class);
    private final ConsultaService consultaService;

    public ConsultaConsumer(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

@RabbitListener(queues = "consulta.finalizada.queue")
    public void onConsultaFinalizada(FinishEvent event) {
        try {
            this.consultaService.finalizarConsulta(event.getId());
            log.info("Consulta {} finalizada com sucesso via mensageria.", event.getId());
        } catch (ResourceNotFoundException e) {
            log.warn("Recebi evento de finalização, mas a consulta {} não existe no banco de agendamento. Mensagem descartada.", event.getId());
        } catch (Exception e) {
            log.error("Erro desconhecido ao processar mensagem: ", e);
            throw e; 
        }
    }

}
