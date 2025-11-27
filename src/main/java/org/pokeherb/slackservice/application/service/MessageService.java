package org.pokeherb.slackservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.slackservice.domain.MessageSend;
import org.pokeherb.slackservice.domain.Message;
import org.pokeherb.slackservice.domain.MessageRepository;
import org.pokeherb.slackservice.domain.MessageStatus;
import org.pokeherb.slackservice.domain.exception.SlackErrorCode;
import org.pokeherb.slackservice.global.infrastructure.exception.CustomException;
import org.pokeherb.slackservice.presentation.dto.SlackDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSend messageSend;
    private final MessageRepository repository;

    @Transactional
    public UUID sendAndSave(UUID receiverUserId, String messageText, String slackUserId) {
        boolean success;
        String errorMessage = null;

        try {
            // 슬랙 메시지 전송
            success = messageSend.send(List.of(slackUserId),  messageText);
        } catch (Exception e) {
            throw new CustomException(SlackErrorCode.SLACK_API_ERROR);
        }

        MessageStatus status = success ? MessageStatus.SUCCESS : MessageStatus.FAILED;

        Message slackMessage = Message.builder()
                .receiverUserId(receiverUserId)
                .message(messageText)
                .status(status)
                .errorMessage(errorMessage)
                .sentAt(LocalDateTime.now())
                .build();

        repository.save(slackMessage);

        return slackMessage.getId();
    }

    @Transactional(readOnly = true)
    public SlackDetailResponse getDetails(UUID id) {
        Message message = repository.findById(id).orElseThrow(()
                -> new CustomException(SlackErrorCode.MESSAGE_NOT_FOUND));

        return new SlackDetailResponse(
                message.getId(),
                message.getReceiverUserId(),
                message.getMessage(),
                message.getStatus(),
                message.getErrorMessage(),
                message.getSentAt()
        );
    }

    @Transactional(readOnly = true)
    public List<SlackDetailResponse> getAll() {
        return repository.findAll().stream()
                .map(m -> new SlackDetailResponse(
                        m.getId(),
                        m.getReceiverUserId(),
                        m.getMessage(),
                        m.getStatus(),
                        m.getErrorMessage(),
                        m.getSentAt()
                ))
                .toList();
    }
}
