package org.pokeherb.slackservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "P_SLACK_MESSAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID receiverUserId;

    @Lob
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Builder
    public Message(UUID id, UUID receiverUserId, String message, MessageStatus status,
                   String errorMessage, LocalDateTime sentAt) {
        this.id = id;
        this.receiverUserId = receiverUserId;
        this.message = message;
        this.status = status;
        this.errorMessage = errorMessage;
        this.sentAt = sentAt;
    }
}
