package org.pokeherb.slackservice.application.service;

import org.pokeherb.slackservice.presentation.dto.DeliveryMessageRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DeliveryMessageService {
    void send(List<Map<UUID, String>> users, DeliveryMessageRequest request);
}