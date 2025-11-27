package org.pokeherb.slackservice.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.slackservice.infrastructure.messaging.handler.SlackEventHandler;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitConsumer {

    private final Map<String, SlackEventHandler> handlers;

    @RabbitListener(queues = "slack")
    public void listen(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        log.info("Received MQ message - routingKey={}, payload={}", routingKey, payload);

        SlackEventHandler handler = handlers.get(routingKey);
        if (handler == null) {
            log.warn("Unhandled routingKey: {}", routingKey);
            return;
        }

        handler.handle(payload);
    }
}
