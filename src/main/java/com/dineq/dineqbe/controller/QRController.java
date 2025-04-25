package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.service.QRService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/register")
public class QRController {

    private final QRService qrService;

    public QRController(QRService qrService) {
        this.qrService = qrService;
    }

    /**
     * 큐알코드를 찍었을 때 첫 동작
     * GET /api/v1/register/QR/{tableId}
     * @param tableId
     * @return
     */
    @GetMapping("/QR/{tableId}")
    public String pictureQR(@PathVariable String tableId) {
        return "redirect:/QR.html?tableId=" + tableId;
    }

    /**
     * tableId 검증 후, 토큰 만들어서 프론트로 리다이렉트
     * POST /api/v1/register/QR/{tableId}
     * @param tableId
     * @return
     */
    @PostMapping("/QR/{tableId}")
    public ResponseEntity<String> registerQR(@PathVariable String tableId) {
        // 해당 테이블이 활성화되어있는지 확인 후 활성화 되어 있지 않다면 특정 페이지로 리다이렉트
        Boolean flag= qrService.checkTable(tableId);
        String redirectURl= "";

        if(!flag) {
            redirectURl = "/QR-fail.html?tableId=" + tableId;

        }else{
            String randomToken= qrService.registerQR(tableId);
            redirectURl="https://dine-q-fe.vercel.app/order?tableId="+tableId+"&token="+randomToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectURl);

        return ResponseEntity.status(302).headers(headers).build();
    }
}
