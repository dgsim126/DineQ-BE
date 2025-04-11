package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.menu.OrderResponseDTO;
import com.dineq.dineqbe.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 요청된, 요리중인, 완료된 주문 조회
     * GET /api/v1/store/orders?status={REQUESTED, COOKING, COMPLETED}
     * @param status
     * @return
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByStatus(@RequestParam String status) {
        List<OrderResponseDTO> response= orderService.getOrderByStatus(status);
        return ResponseEntity.ok(response);
    }

    /**
     * 주문 요청 수락(요청된 주문 → 요리중인 주문)
     * PUT /api/v1/store/orders/{orderId}/accept
     * @param orderId
     * @return
     */
    @PutMapping("/orders/{orderId}/accept")
    public ResponseEntity<String> acceptOrder(@PathVariable Long orderId) {
        try{
            orderService.acceptOrder(orderId);
            return ResponseEntity.ok("요청된 주문이 요리중인 상태로 넘어갔습니다.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * 조리 완료 (요리중인 주문 → 완료된 주문)
     * PUT /api/v1/store/orders/{orderId}/complete
     * @param orderId
     * @return
     */
    @PutMapping("/orders/{orderId}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long orderId) {
        try{
            orderService.completeOrder(orderId);
            return ResponseEntity.ok("요리중인 주문이 완료된 주문으로 넘어갔습니다.");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
