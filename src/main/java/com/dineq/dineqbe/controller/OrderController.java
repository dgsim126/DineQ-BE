package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.menu.OrderResponseDTO;
import com.dineq.dineqbe.dto.menu.OrderStatusUpdateRequestDTO;
import com.dineq.dineqbe.dto.menu.OrderStatusUpdateResponseDTO;
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
    public ResponseEntity<List<List<OrderResponseDTO>>> getOrderByStatus(@RequestParam String status) {
        List<List<OrderResponseDTO>> response= orderService.getOrderByStatus(status);
        return ResponseEntity.ok(response);
    }

    /**
     * 주문 요청 수락 (요청된 주문 → 요리중인 주문)
     * PUT /api/v1/store/orders/accept
     * @param request
     * @return
     */
    @PutMapping("/orders/accept")
    public ResponseEntity<OrderStatusUpdateResponseDTO> acceptOrders(@RequestBody OrderStatusUpdateRequestDTO request) {
        OrderStatusUpdateResponseDTO result = orderService.acceptOrders(request.getOrderId());
        return ResponseEntity.ok(result);
    }

    /**
     * 조리 완료 (요리중인 주문 → 완료된 주문)
     * PUT /api/v1/store/orders/complete
     * @param request
     * @return
     */
    @PutMapping("/orders/complete")
    public ResponseEntity<OrderStatusUpdateResponseDTO> completeOrders(@RequestBody OrderStatusUpdateRequestDTO request) {
        OrderStatusUpdateResponseDTO result = orderService.completeOrders(request.getOrderId());
        return ResponseEntity.ok(result);
    }

    /**
     * 개별 주문 취소
     * DELETE /api/v1/store/orders/one/{orderId}/cancel
     * @param orderId
     * @return
     */
    @DeleteMapping("/orders/one/{orderId}/cancel")
    public ResponseEntity<String> cancelOneOrder(@PathVariable Long orderId) {
        try{
            orderService.cancelOneOrder(orderId);
            return ResponseEntity.ok(orderId + " 주문이 취소되었습니다");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * 전체 주문 취소
     * DELETE /api/v1/store/orders/all/{groupNum}/cancel
     * @param groupNum
     * @return
     */
    @DeleteMapping("/orders/all/{groupNum}/cancel")
    public ResponseEntity<String> cancelAll(@PathVariable String groupNum) {
        try{
            orderService.cancelAll(groupNum);
            return ResponseEntity.ok(groupNum + " 주문이 취소되었습니다");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
