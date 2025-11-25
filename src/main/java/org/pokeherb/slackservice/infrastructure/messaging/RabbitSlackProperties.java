package org.pokeherb.slackservice.infrastructure.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbit.slack")
public record RabbitSlackProperties(
        String exchange,
        String queue,
        String routingKey
) {}
