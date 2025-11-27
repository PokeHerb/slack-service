package org.pokeherb.slackservice.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.slackservice.application.service.MessageService;
import org.pokeherb.slackservice.presentation.dto.SlackDetailResponse;
import org.pokeherb.slackservice.presentation.dto.SlackSendRequest;
import org.pokeherb.slackservice.presentation.dto.SlackSendResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SlackMessageController {

    private final MessageService messageService;

    // 로그인한 모든 사용자 발송 가능
    @PostMapping("messages")
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

    // 조회 및 검색 (관리자만)

    @GetMapping("/{messageId}")
    @PreAuthorize("hasRole('MASTER')")
    public SlackDetailResponse getOne(@PathVariable UUID messageId) {
        return messageService.getDetails(messageId);
    }

    @GetMapping
    @PreAuthorize("hasRole('MASTER')")
    public List<SlackDetailResponse> getAll() {
        return messageService.getAll();
    }
}
