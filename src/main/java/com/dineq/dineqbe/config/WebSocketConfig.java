package com.dineq.dineqbe.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 클라이언트가 최초로 접속할 웹소켓 엔드포인트(URL) 등록
     * @param registry
     */
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        // 프론트에서 ws://localhost:8080/ws 로 연결
//        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns("*"); // CORS 개념과 유사. 어떤 Origin에서 오는 핸드셰이크 요청을 허용할지 (로컬)
//                // .setAllowedOriginPatterns("*"); // 실제 운영 시 특정 도메인만 허용하도록 변경할 것
//    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
                        "https://dine-q-fe-renewer.vercel.app",
                        "https://dineqws.duckdns.org"
                );
    }


    /**
     * STOMP 목적지 체계 설정, 메시지를 어디로 보내고 받는지 주소 규칙 정하기
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");        // 서버 -> 클라이언트
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트 -> 서버
    }
}
