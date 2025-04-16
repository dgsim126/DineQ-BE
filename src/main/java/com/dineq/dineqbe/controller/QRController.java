package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.service.QRService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register")
public class QRController {

    private final QRService qrService;

    public QRController(QRService qrService) {
        this.qrService = qrService;
    }

    /**
     * 토큰 만들어서 프론트로 리다이렉트
     * POST /api/v1/register/QR/{tableId}
     * @param tableId
     * @return
     */
    @PostMapping("/QR/{tableId}")
    public ResponseEntity<String> registerQR(@PathVariable String tableId) {
        String randomToken= qrService.registerQR(tableId);

        // String redirectURl="http://localhost:3000/order/"+tableId+"?token="+randomToken;
        String redirectURl="https://honorsparking-web.vercel.app/"+tableId+"?token="+randomToken;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectURl);

        return ResponseEntity.status(302).headers(headers).build();
    }
}
