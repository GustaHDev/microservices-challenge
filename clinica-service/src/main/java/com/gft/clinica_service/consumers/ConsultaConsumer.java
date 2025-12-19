package com.gft.clinica_service.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gft.clinica_service.dtos.CancelEvent;
import com.gft.clinica_service.services.ConsultaService;

@Component
public class ConsultaConsumer {

    private final ConsultaService consultaService;

    public ConsultaConsumer(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @RabbitListener(queues = "consulta.cancelada.queue")
    public void onConsultaCancelada(CancelEvent event) {
        this.consultaService.cancelarConsulta(event.getId());
    }

}
