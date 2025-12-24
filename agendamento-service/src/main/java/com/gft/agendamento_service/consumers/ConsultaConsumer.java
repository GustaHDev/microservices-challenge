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
            // AQUI ESTÁ O PULO DO GATO:
            // Nós logamos o erro, mas NÃO lançamos a exceção novamente.
            // O método termina "com sucesso", o RabbitMQ recebe o ACK e remove a mensagem da fila.
            log.warn("Recebi evento de finalização, mas a consulta {} não existe no banco de agendamento. Mensagem descartada.", event.getId());
        } catch (Exception e) {
            // Para outros erros (banco fora do ar, etc), talvez você queira que retente.
            // Nesse caso, lançar a exceção fará o loop continuar (o que pode ser desejado para erros temporários).
            log.error("Erro desconhecido ao processar mensagem: ", e);
            throw e; 
        }
    }

}
