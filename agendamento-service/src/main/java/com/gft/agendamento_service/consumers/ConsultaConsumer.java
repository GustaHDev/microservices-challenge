package com.gft.agendamento_service.consumers;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gft.agendamento_service.services.ConsultaService;

@Component
public class ConsultaConsumer {

    private final ConsultaService consultaService;

    public ConsultaConsumer(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @RabbitListener(queues = "consulta.finalizada.queue")
    public void onConsultaFinalizada(UUID consultaId) {
        this.consultaService.finalizarConsulta(consultaId);
    }

}
