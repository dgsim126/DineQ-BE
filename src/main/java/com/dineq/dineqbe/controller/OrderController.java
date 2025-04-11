package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.menu.OrderResponseDTO;
import com.dineq.dineqbe.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByStatus(@RequestParam String status) {
        List<OrderResponseDTO> response= orderService.getOrderByStatus(status);
        return ResponseEntity.ok(response);
    }
}
