package org.pokeherb.slackservice.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.slackservice.application.service.DeliveryMessageService;
import org.pokeherb.slackservice.application.service.MessageService;
import org.pokeherb.slackservice.presentation.dto.DeliveryMessageRequest;
import org.pokeherb.slackservice.presentation.dto.SlackDetailResponse;
import org.pokeherb.slackservice.presentation.dto.SlackSendRequest;
import org.pokeherb.slackservice.presentation.dto.SlackSendResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SlackMessageController {

    private final MessageService messageService;
    private final DeliveryMessageService deliveryMessageService;

    // 슬랙 메시지 전송 (로그인 사용자)
    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('...')")
    public SlackSendResponse send(@RequestBody SlackSendRequest request) {
        UUID id = messageService.sendAndSave(
                request.receiverUserId(),
                request.message(),
                request.slackUserId()
        );
        return new SlackSendResponse(id);
    }

    // 메시지 상세 조회 (관리자 전용)
    @GetMapping("/{messageId}")
    @PreAuthorize("hasRole('MASTER')")
    public SlackDetailResponse getOne(@PathVariable UUID messageId) {
        return messageService.getDetails(messageId);
    }

    // 메시지 목록 조회 (관리자 전용)
    @GetMapping
    @PreAuthorize("hasRole('MASTER')")
    public List<SlackDetailResponse> getAll() {
        return messageService.getAll();
    }

    // 배송 정보 기반 안내 메시지 생성 후 Slack 발송 (AI)
    @PostMapping("/delivery/message")
    public void sendDeliveryMessage(@RequestBody DeliveryMessageRequest request) {
        deliveryMessageService.send(List.of(Map.of(request.receiverId(), request.slackId().toString())), request);
    }
}
