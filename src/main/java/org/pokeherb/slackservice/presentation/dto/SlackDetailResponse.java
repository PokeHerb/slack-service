package org.pokeherb.slackservice.presentation.dto;

import org.pokeherb.slackservice.domain.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SlackDetailResponse(
        UUID id,
        UUID receiverUserId,
        String message,
        MessageStatus status,
        String errorMessage,
        LocalDateTime sentAt
) {}
