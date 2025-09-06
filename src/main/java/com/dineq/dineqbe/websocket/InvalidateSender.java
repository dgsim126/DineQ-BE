package com.dineq.dineqbe.websocket;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class InvalidateSender {

    /**
     * 하나의 대기 중 메시지를 표현하기 위한 클래스
     */
    private static class Pending {
        final Map<String, Object> payload;
        int attempts; // 시도 횟수
        Pending(Map<String, Object> payload, int attempts) {
            this.payload= payload;
            this.attempts= attempts;
        }
    }

    private static final String DESTINATION = "/topic/orders"; // 매장 1개이므로 고정

    // 서버->클라이언트로 STOMP 메시지를 발송하는 템플릿
    private final SimpMessagingTemplate messaging;

    // 아직 ACK가 도착하지 않은 대기 중인 메시지를 보관
    private final Map<String, Pending> pendings= new ConcurrentHashMap<>();

    // 재전송 타이머를 관리하는 단일 스레드 스케줄러
    private final ScheduledExecutorService ses= Executors.newSingleThreadScheduledExecutor();

    /**
     * 클라이언트에게 "변경됨" 신호 1개를 발사한다.
     */
    public void sendAlert() { // 알림 1건 발사
        String id= UUID.randomUUID().toString();
        Map<String, Object> payload= Map.of(
                "type", "invalidate", // 내용 없음
                "messageId", id       // ACK 식별용
        );
        pendings.put(id, new Pending(payload, 0)); // pendings Map에 넣어둔다.
        doSend(id);
    }

    private void doSend(String id) { // 실제 전송 + 타임아웃 후 재전송
        Pending p= pendings.get(id); // 객체를 꺼내온다.

        // 이미 ACK가 와서 제거된 경우
        if(p==null) {
            return;
        }

        // 서버에서 클라이언트로 브로드캐스트 : 구독경로는 설정한 DESTINATION
        messaging.convertAndSend(DESTINATION, p.payload);

        ses.schedule(() -> {
            Pending now= pendings.get(id);
            if(now==null) { // ACK 도착
                return;
            }
            if(now.attempts>=5) { // 최대 5회 후 포기
                pendings.remove(id);
                return;
            }
            now.attempts+=1;
            doSend(id); // 재알림
        }, retryDelaySeconds(p.attempts), TimeUnit.SECONDS);
    }

    /**
     * 재전송 지연 시간(초)를 계산한다.
     * @param attempts= 현재까지의 시도 횟수
     * @return= 다음 전송까지 대기할 초
     */
    private long retryDelaySeconds(int attempts) {
        // 3, 6, 12, 24, 48초...
        return (long) (3 * Math.pow(2, Math.max(0, attempts - 1)));
    }

    /**
     * 프론트로부터 수신 확인(ACK)이 오면 대기 목록에서 제거한다.
     * - AckController(@MessageMapping("/ack"))에서 이 메서드를 호출
     * @param messageId
     */
    public void onAck(String messageId) {
        pendings.remove(messageId);
    }

    /**
     * 애플리케이션 종료시 스레드가 남아 있을 수 있으니 종료
     */
    @PreDestroy
    public void shutdown() {
        ses.shutdown();
    }
}

