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

    @PostMapping("/QR/{tableId}")
    public ResponseEntity<String> registerQR(@PathVariable String tableId) {
        String randomToken= qrService.registerQR(tableId);

        // String redirectURl="http://localhost:3000/"+tableId+"?token="+randomToken;
        String redirectURl="https://honorsparking-web.vercel.app/"+tableId+"?token="+randomToken;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectURl);

        return ResponseEntity.status(302).headers(headers).build();
    }
}
