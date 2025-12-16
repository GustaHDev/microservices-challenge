package com.gft.procedimento_service.config;

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

    private static final String PROCEDIMENTO_EXCHANGE = "exchange.procedimentos";
    private static final String PROCEDIMENTO_ROUTING_KEY = "procedimento.cancelado";
    private static final String PROCEDIMENTO_QUEUE = "procedimento.cancelado.queue";

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }

    @Bean
    public TopicExchange procedimentoExchange() {
        return new TopicExchange(PROCEDIMENTO_EXCHANGE);
    }

    @Bean
    public Queue procedimentoCanceladoQueue() {
        return new Queue(PROCEDIMENTO_QUEUE);
    }

    @Bean
    public Binding procedimentoBinding(Queue procedimentoCanceladoQueue, TopicExchange procedimentoExchange) {
        return BindingBuilder.bind(procedimentoCanceladoQueue).to(procedimentoExchange).with(PROCEDIMENTO_ROUTING_KEY);
    }

}
