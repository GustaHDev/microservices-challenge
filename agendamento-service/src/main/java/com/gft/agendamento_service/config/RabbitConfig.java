package com.gft.agendamento_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitConfig {

    private static final String CONSULTA_EXCHANGE = "exchange.consultas";
    private static final String CONSULTA_ROUTING_KEY = "consulta.finalizada";
    private static final String CONSULTA_QUEUE = "consulta.finalizada.queue";
    
    private static final String PROCEDIMENTO_EXCHANGE = "exchange.procedimentos";
    private static final String PROCEDIMENTO_ROUTING_KEY = "procedimento.finalizado";
    private static final String PROCEDIMENTO_QUEUE = "procedimento.finalizado.queue";

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }

    @Bean
    public TopicExchange consultaExchange() {
        return new TopicExchange(CONSULTA_EXCHANGE);
    }

    @Bean
    public TopicExchange procedimentoExchange() {
        return new TopicExchange(PROCEDIMENTO_EXCHANGE);
    }

    @Bean
    public Queue consultaFinalizadaQueue() {
        return new Queue(CONSULTA_QUEUE);
    }

    @Bean
    public Queue procedimentoFinalizadoQueue() {
        return new Queue(PROCEDIMENTO_QUEUE);
    }

    @Bean
    public Binding consultaBinding(Queue consultaFinalizadaQueue, TopicExchange consultaExchange) {
        return BindingBuilder.bind(consultaFinalizadaQueue).to(consultaExchange).with(CONSULTA_ROUTING_KEY);
    }

    @Bean
    public Binding procedimentoBinding(Queue procedimentoFinalizadoQueue, TopicExchange procedimentoExchange) {
        return BindingBuilder.bind(procedimentoFinalizadoQueue).to(procedimentoExchange).with(PROCEDIMENTO_ROUTING_KEY);
    }

}
