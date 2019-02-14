package com.lambdaschool.languages;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LanguagesApplication {
  public static final String EXCHANGE_NAME = "Language";
  public static final String QUEUE_NAME = "Log";

  public static void main(String[] args) {
    SpringApplication.run(LanguagesApplication.class, args);
  }

  // Instantiate exchange and queue
  @Bean
  public TopicExchange appExchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public Queue appQueue() {
    return new Queue(QUEUE_NAME);
  }

  // Bind queue to exchange
  @Bean
  public Binding declareBinding() {
    return BindingBuilder.bind(appQueue()).to(appExchange()).with(QUEUE_NAME);
  }

  // Setup Jackson2JsonMessageConverter
  @Bean
  public Jackson2JsonMessageConverter producerJackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(final ConnectionFactory cf) {
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(cf);
    rabbitTemplate.setMessageConverter(producerJackson2JsonMessageConverter());
    return rabbitTemplate;
  }
}

