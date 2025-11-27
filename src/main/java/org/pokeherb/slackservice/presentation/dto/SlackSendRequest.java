package org.pokeherb.slackservice.presentation.dto;

import java.util.UUID;

public record SlackSendRequest(
        UUID receiverUserId,
        String slackUserId,
        String message
) {}
