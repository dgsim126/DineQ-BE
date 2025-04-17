package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.QREntity;
import com.dineq.dineqbe.repository.QRRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class QRService {

    private final QRRepository qrRepository;

    public QRService(QRRepository qrRepository) {
        this.qrRepository = qrRepository;
    }

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int RANDOM_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();


    // 랜덤 토큰 생성
    public String generateRandomToken(){
        String timestamp= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            randomPart.append(CHAR_POOL.charAt(index));
        }

        return timestamp + randomPart;
    }

    // 랜덤 토큰 생성
    public String registerQR(String tableId) {
        String token= generateRandomToken();
        System.out.println("현재 생성된 랜덤 토큰:" + (token));

        QREntity qrEntity = new QREntity();
        qrEntity.setToken(token);
        qrEntity.setTableId(tableId);

        qrRepository.save(qrEntity);
        return token;
    }

    // token, tableId값이 유효한지 데이터베이스에 접근하여 검증
    public void verifyToken(String token, String tableId) {
        if(token == null || token.isEmpty() || tableId == null || tableId.isEmpty()){
            System.out.println("헤더에 값이 비어있음");
            // return false;
            throw new IllegalArgumentException("헤더에 값이 비어있음");
        }
        if(!qrRepository.existsByTokenAndTableId(token, tableId)){
            throw new AuthenticationCredentialsNotFoundException("token, tableId에 해당하는 튜플이 존재하지 않음");
        }
    }

    // 30분이 지난 QR 엔티티 삭제
    @Scheduled(fixedRate = 600_000)
    @Transactional
    public void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now().minusMinutes(30);
        qrRepository.deleteByCreatedAtBefore(now);
        System.out.println("30분 이상 지난 QR 토큰 삭제 완료 at " + LocalDateTime.now());
    }
}
