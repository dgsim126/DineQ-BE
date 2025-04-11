package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import com.dineq.dineqbe.domain.enums.OrderStatus;
import com.dineq.dineqbe.dto.menu.OrderResponseDTO;
import com.dineq.dineqbe.repository.TableOrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final TableOrderRepository tableOrderRepository;

    public OrderService(TableOrderRepository tableOrderRepository) {
        this.tableOrderRepository = tableOrderRepository;
    }

    public List<OrderResponseDTO> getOrderByStatus(String status) {

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("주문 상태 값이 비어 있습니다.");
        }

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<TableOrderEntity> tableOrderEntities = tableOrderRepository.findByStatus(orderStatus);

            List<OrderResponseDTO> result = new ArrayList<>();

            for (TableOrderEntity entity : tableOrderEntities) {
                OrderResponseDTO dto = OrderResponseDTO.fromEntity(entity);
                result.add(dto);
            }

            return result;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 주문 상태: " + status);
        }

    }
}
