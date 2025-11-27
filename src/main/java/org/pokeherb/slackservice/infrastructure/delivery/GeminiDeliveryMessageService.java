package org.pokeherb.slackservice.infrastructure.delivery;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.pokeherb.slackservice.application.service.DeliveryMessageService;
import org.pokeherb.slackservice.application.service.MessageService;
import org.pokeherb.slackservice.presentation.dto.DeliveryMessageRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeminiDeliveryMessageService implements DeliveryMessageService {

    // 실제 모델 호출에 사용할 ChatClient
    private ChatClient client;

    // 프롬프트 템플릿 파일
    private Resource resource;

    // Spring AI에서 주입해주는 ChatClient 빌더
    private final ChatClient.Builder builder;

    private final MessageService messageService;

    @PostConstruct
    public void setup() {
        resource = new ClassPathResource("prompt.txt");
        client = builder.build();
    }

    /**
     * 배송 관련 Slack 메시지 생성 후 지정된 유저에게 전송
     *
     * @param users   메시지 수신자 목록
     * @param request Slack 프롬프트에 채워 넣을 배송 정보 DTO
     */
    @Override
    public void send(List<Map<UUID, String>> users, DeliveryMessageRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 프롬프트 템플릿에 바인딩할 파라미터 맵
        Map<String, Object> params = new HashMap<>();
        params.put("order_no", request.orderNo().toString());
        params.put("receiver_name", request.receiverName());
        params.put("order_date", formatter.format(request.orderDate()));
        params.put("product_name", request.productName());
        params.put("due_at", request.dueAt());
        params.put("start_hub", request.startHub());
        params.put("stopover_hub", request.stopoverHub());
        params.put("arrival_address", request.arrivalAddress());
        params.put("delivery_driver_name", request.deliveryDriverName());
        params.put("final_duration", request.finalDuration() + "시간");

        // AI 메시지 생성
        String message = client.prompt()
                .user(s -> s.text(resource)
                        .params(params))
                .call()
                .content();

        // 여러 사용자에게 메시지 전송
        users.forEach(user -> {
            Map.Entry<UUID, String> entry = user.entrySet().iterator().next();
            messageService.sendAndSave(entry.getKey(), message, entry.getValue());
        });

    }
}
