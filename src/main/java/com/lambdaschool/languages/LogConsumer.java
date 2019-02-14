package com.lambdaschool.languages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
//@Service
public class LogConsumer {
  // You're going to be a listener and this is what you're going to listen for
  @RabbitListener(queues = LanguagesApplication.QUEUE_NAME)
  public void consumeMessage(final Message msg) {
    log.info("Received Message: {}", msg.toString());
  }
}
