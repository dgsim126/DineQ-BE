package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import com.dineq.dineqbe.domain.enums.OrderStatus;
import com.dineq.dineqbe.dto.menu.OrderResponseDTO;
import com.dineq.dineqbe.dto.menu.OrderStatusUpdateResponseDTO;
import com.dineq.dineqbe.repository.TableOrderRepository;
import com.dineq.dineqbe.websocket.InvalidateSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final TableOrderRepository tableOrderRepository;
    private final InvalidateSender invalidateSender;

    public OrderService(TableOrderRepository tableOrderRepository, InvalidateSender invalidateSender) {
        this.tableOrderRepository = tableOrderRepository;
        this.invalidateSender = invalidateSender;
    }


    // status에 따른 주문 내역 확인
    public List<List<OrderResponseDTO>> getOrderByStatus(String status) {

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("주문 상태 값이 비어 있습니다.");
        }

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<TableOrderEntity> tableOrderEntities = tableOrderRepository.findByStatus(orderStatus);

            // Entity -> DTO
            List<OrderResponseDTO> dtoList = tableOrderEntities.stream()
                    .map(OrderResponseDTO::fromEntity)
                    .toList();

            // groupNum 기준으로 그룹핑
            Map<String, List<OrderResponseDTO>> grouped = dtoList.stream()
                    .collect(Collectors.groupingBy(OrderResponseDTO::getGroupNum));

            // 그룹핑된 리스트를 orderTime 기준으로 내림차순 정렬
            return grouped.values().stream()
                    .sorted((g1, g2) -> g2.get(0).getOrderTime().compareTo(g1.get(0).getOrderTime()))
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 주문 상태: " + status);
        }

    }

    // 요청된주문 -> 요리중인 주문
    @Transactional
    public OrderStatusUpdateResponseDTO acceptOrders(List<Long> orderIds) {
        List<Long> success = new ArrayList<>();
        List<OrderStatusUpdateResponseDTO.FailedOrderDTO> failed = new ArrayList<>();
        List<TableOrderEntity> toSave = new ArrayList<>();

        for (Long orderId : orderIds) {
            Optional<TableOrderEntity> optionalOrder = tableOrderRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                failed.add(new OrderStatusUpdateResponseDTO.FailedOrderDTO(orderId, "주문이 존재하지 않습니다."));
                continue;
            }

            TableOrderEntity order = optionalOrder.get();

            if (order.getStatus() != OrderStatus.REQUESTED) {
                failed.add(new OrderStatusUpdateResponseDTO.FailedOrderDTO(orderId, "해당 주문은 REQUESTED 상태가 아닙니다."));
                continue;
            }

            order.setStatus(OrderStatus.COOKING);
            toSave.add(order);
            success.add(orderId);
        }

        if (!toSave.isEmpty()) {
            tableOrderRepository.saveAll(toSave);

            // 커밋 성공 후 신호 발사 (웹소켓)
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() {
                    invalidateSender.sendAlert("/api/v1/store/orders/accept");
                }
            });
        }

        return new OrderStatusUpdateResponseDTO(success, failed);
    }


    // 요리중인 주문 -> 완료된 주문
    @Transactional
    public OrderStatusUpdateResponseDTO completeOrders(List<Long> orderIds) {
        List<Long> success = new ArrayList<>();
        List<OrderStatusUpdateResponseDTO.FailedOrderDTO> failed = new ArrayList<>();
        List<TableOrderEntity> toSave = new ArrayList<>();

        for (Long orderId : orderIds) {
            Optional<TableOrderEntity> optionalOrder = tableOrderRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                failed.add(new OrderStatusUpdateResponseDTO.FailedOrderDTO(orderId, "주문이 존재하지 않습니다."));
                continue;
            }

            TableOrderEntity order = optionalOrder.get();

            if (order.getStatus() != OrderStatus.COOKING) {
                failed.add(new OrderStatusUpdateResponseDTO.FailedOrderDTO(orderId, "해당 주문은 COOKING 상태가 아닙니다."));
                continue;
            }

            order.setStatus(OrderStatus.COMPLETED);
            toSave.add(order);
            success.add(orderId);
        }

        if (!toSave.isEmpty()) {
            tableOrderRepository.saveAll(toSave);

            // 커밋 성공 후 신호 발사 (웹소켓)
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() {
                    invalidateSender.sendAlert("/api/v1/store/orders/complete");
                }
            });
        }

        return new OrderStatusUpdateResponseDTO(success, failed);
    }


    // orderId에 해당하는 주문내역 지우기
    @Transactional
    public void cancelOneOrder(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId가 비어있음");
        }

        TableOrderEntity tableOrderEntity= tableOrderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("orderId "+ orderId +"에 해당되는 주문이 존재하지 않음"));

        tableOrderRepository.delete(tableOrderEntity);

        // 커밋 성공 후 신호 발사 (웹소켓)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                invalidateSender.sendAlert("/api/v1/store/orders/one/{orderId}/cancel");
            }
        });
    }

    // groupNum에 해당하는 주문내역 지우기
    @Transactional
    public void cancelAll(String groupNum) {
        if(groupNum == null) {
            throw new IllegalArgumentException("groupNum이 비어있음");
        }

        List<TableOrderEntity> tableOrderEntities = tableOrderRepository.findAllByGroupNum(groupNum);

        if(tableOrderEntities.isEmpty()) {
            throw new IllegalArgumentException(groupNum + " groupNum에 해당하는 주문내역이 존재하지 않음");
        }
        tableOrderRepository.deleteAll(tableOrderEntities);

        // 커밋 성공 후 신호 발사 (웹소켓)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                invalidateSender.sendAlert("/api/v1/store/orders/all/{groupNum}/cancel");
            }
        });
    }
}
