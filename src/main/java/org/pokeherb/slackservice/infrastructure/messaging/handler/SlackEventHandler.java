package org.pokeherb.slackservice.infrastructure.messaging.handler;

public interface SlackEventHandler {
    void handle(String payload);
}
