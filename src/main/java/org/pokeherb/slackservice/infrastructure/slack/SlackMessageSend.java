package org.pokeherb.slackservice.infrastructure.slack;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.slackservice.domain.MessageSend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RefreshScope
public class SlackMessageSend implements MessageSend {
    @Value("${slack.token}")
    private String token;

    @Override
    public boolean send(List<String> ids, String message) {
        if (ids != null) {
            RestClient client = RestClient.builder()
                            .baseUrl("https://slack.com/api")
                            .build();

            // Channel ID 처리
            ResponseEntity<JsonNode> response = client.post()
                        .uri("/conversations.open")
                    .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Map.of("users", String.join(",", ids)))
                    .retrieve()
                    .toEntity(JsonNode.class);
            JsonNode node = response.getBody();

            if (!response.getStatusCode().is2xxSuccessful() || node.get("ok") == null
                    || !node.get("ok").toString().equals("true")) return false;

            String channelId = node.get("channel").get("id").textValue();


            // 메시지 발송 처리
            response = client.post()
                    .uri("/chat.postMessage")
                    .header("Authorization", "Bearer " + token)
                    .body(Map.of("channel", channelId, "text", message, "as_user", true))
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(JsonNode.class);
            return response.getStatusCode().is2xxSuccessful() && node.get("ok") != null && node.get("ok").toString().equals("true");
        }

        return false;
    }
}
