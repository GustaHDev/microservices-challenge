package com.gft.clinica_service.config;

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

    private static final String CONSULTA_FINALIZADA_ROUTING_KEY = "consulta.finalizada";
    private static final String CONSULTA_FINALIZADA_QUEUE = "consulta.finalizada.queue";

    private static final String CONSULTA_CANCELADA_ROUTING_KEY = "consulta.cancelada";
    private static final String CONSULTA_CANCELADA_QUEUE = "consulta.cancelada.queue";

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
    public Queue consultaFinalizadaQueue() {
        return new Queue(CONSULTA_FINALIZADA_QUEUE);
    }

    @Bean
    public Binding consultaFinalizadaBinding(Queue consultaFinalizadaQueue, TopicExchange consultaExchange) {
        return BindingBuilder.bind(consultaFinalizadaQueue).to(consultaExchange).with(CONSULTA_FINALIZADA_ROUTING_KEY);
    }

    @Bean
    public Queue consultaCanceladaQueue() {
        return new Queue(CONSULTA_CANCELADA_QUEUE);
    }

    @Bean
    public Binding consultaCanceladaBinding(Queue consultaCanceladaQueue, TopicExchange consultaExchange) {
        return BindingBuilder.bind(consultaCanceladaQueue).to(consultaExchange).with(CONSULTA_CANCELADA_ROUTING_KEY);
    }

}
