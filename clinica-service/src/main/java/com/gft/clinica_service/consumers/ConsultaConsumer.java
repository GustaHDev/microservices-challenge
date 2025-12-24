package com.gft.clinica_service.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gft.clinica_service.dtos.CancelEvent;
import com.gft.clinica_service.exceptions.ResourceNotFoundException;
import com.gft.clinica_service.services.ConsultaService;

@Component
public class ConsultaConsumer {

    private final Logger log = LoggerFactory.getLogger(ConsultaConsumer.class);

    private final ConsultaService consultaService;

    public ConsultaConsumer(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

@RabbitListener(queues = "consulta.cancelada.queue")
    public void onConsultaCancelada(CancelEvent event) {
        try {
            if (event.getId() == null) {
                log.warn("Evento de cancelamento recebido com ID nulo. Ignorando.");
                return;
            }

            log.info("Recebido pedido de cancelamento para o agendamento: {}", event.getId());
            
            this.consultaService.cancelarConsulta(event.getId());
            
            log.info("Consulta vinculada ao agendamento {} foi cancelada.", event.getId());

        } catch (ResourceNotFoundException e) {
            log.warn("Não foi possível cancelar: Consulta vinculada ao agendamento {} não encontrada no banco da clínica.", event.getId());
        } catch (Exception e) {
            log.error("Erro inesperado ao processar cancelamento: ", e);
        }
    }

}
