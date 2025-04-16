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
     * @param model
     * @return
     */
    @GetMapping("/QR/{tableId}")
    public String pictureQR(@PathVariable String tableId, Model model) {
        model.addAttribute("tableId", tableId);
        return "redirect:/QR.html";
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
        // String redirectURl="https://honorsparking-web.vercel.app/"+tableId+"?token="+randomToken;
        String redirectURl = "https://localhost:3000/order?tableId=" + tableId + "&token=" + randomToken;


        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectURl);

        return ResponseEntity.status(302).headers(headers).build();
    }
}
