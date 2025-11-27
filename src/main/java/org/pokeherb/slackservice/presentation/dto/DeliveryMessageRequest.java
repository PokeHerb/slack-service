package org.pokeherb.slackservice.presentation.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record DeliveryMessageRequest(
        UUID receiverId,
        UUID slackId,
        UUID orderNo,
        String receiverName,
        LocalDateTime orderDate,
        String productName,
        LocalDateTime dueAt,
        String startHub,
        List<String> stopoverHub,
        String arrivalAddress,
        String deliveryDriverName,
        double finalDuration
) {}
