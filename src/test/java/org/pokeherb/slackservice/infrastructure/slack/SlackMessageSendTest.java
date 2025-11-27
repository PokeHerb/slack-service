package org.pokeherb.slackservice.infrastructure.slack;

import org.junit.jupiter.api.Test;
import org.pokeherb.slackservice.domain.MessageSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class SlackMessageSendTest {
    @Autowired
    MessageSend messageSend;

    @Test
    void messageSendTest() {
        String testSlackUserId = System.getenv("SLACK_TEST_USER_ID");
        messageSend.send(List.of(testSlackUserId), "테스트 메시지 전송 성공");
    }
}
