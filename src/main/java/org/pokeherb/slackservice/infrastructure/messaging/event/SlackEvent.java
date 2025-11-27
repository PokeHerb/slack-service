package org.pokeherb.slackservice.infrastructure.messaging.event;

import lombok.extern.slf4j.Slf4j;
import org.pokeherb.slackservice.application.service.MessageService;
import org.pokeherb.slackservice.infrastructure.messaging.handler.AbstractSlackEventHandler;
import org.pokeherb.slackservice.presentation.dto.SlackSendRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component("slack.create")
public class SlackEvent extends AbstractSlackEventHandler {

    private final MessageService messageService;

    public SlackEvent(
            com.fasterxml.jackson.databind.ObjectMapper objectMapper,
            MessageService messageService
    ) {
        super(objectMapper);
        this.messageService = messageService;
    }

    public void handle(String payload) {
        // 공통 메서드로 JSON → DTO 변환
        SlackSendRequest dto =
                readPayload(payload, SlackSendRequest.class);

        UUID receiverUserId = dto.receiverUserId();
        String messageText = dto.message();
        String slackUserId = dto.slackUserId();

        // 도메인 서비스 호출
        messageService.sendAndSave(receiverUserId, messageText, slackUserId);
    }
}