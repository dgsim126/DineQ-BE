package com.dineq.dineqbe.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AckController {

    private final InvalidateSender sender;

    // dto
    public record AckMessage(String messageId) {}

    /**
     * 클라이언트가 "app/ack"로 publish하면 매핑되는 핸들러
     * - WebSocketConfig.configureMessageBroker()에서
     *   setApplicationDestinationPrefixes("/app") 가 설정되어 있어야 이 경로가 활성화됨.
     * @param msg
     */
    @MessageMapping("/ack")
    public void ack(@Payload AckMessage msg) {

        if(msg!=null && msg.messageId()!=null) {
            System.out.println("[ACK Received] AckMessage: " + msg.messageId());
            sender.onAck(msg.messageId()); // 대기 제거
        }else{
            System.out.println("[ACK Received] AckMessage: Something wrong");
        }
    }
}

