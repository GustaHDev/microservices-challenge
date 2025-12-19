package com.gft.agendamento_service.consumers;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gft.agendamento_service.dtos.FinishEvent;
import com.gft.agendamento_service.services.ConsultaService;

@Component
public class ConsultaConsumer {

    private final ConsultaService consultaService;

    public ConsultaConsumer(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @RabbitListener(queues = "consulta.finalizada.queue")
    public void onConsultaFinalizada(FinishEvent event) {
        this.consultaService.finalizarConsulta(event.getId());
    }

}
