package org.pokeherb.slackservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.slackservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SlackErrorCode implements BaseErrorCode {
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "SLACK404", "해당 슬랙 메시지를 찾을 수 없습니다."),
    SLACK_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SLACK500", "슬랙 API 호출 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
