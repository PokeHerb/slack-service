package org.pokeherb.slackservice.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.pokeherb.slackservice.infrastructure.messaging.RabbitSlackProperties;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitSlackProperties.class)
public class RabbitConfig {
    private final RabbitSlackProperties slackProperties;

    @Bean
    public TopicExchange slackExchange() {
        return new TopicExchange(slackProperties.exchange(), true, false);
    }

    @Bean
    public Queue slackQueue() {
        return QueueBuilder.durable(slackProperties.queue()).build();
    }

    @Bean
    public Binding slackBinding(Queue slackQueue, TopicExchange slackExchange) {
        return BindingBuilder.bind(slackQueue).to(slackExchange)
                .with(slackProperties.routingKey());
    }
}
