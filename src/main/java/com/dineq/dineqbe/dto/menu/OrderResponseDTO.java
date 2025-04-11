package com.dineq.dineqbe.dto.menu;

import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import com.dineq.dineqbe.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private String menuName;
    private Integer quantity;
    private Integer totalPrice;
    private LocalDateTime orderTime;
    private OrderStatus status;
    private Long tableId;
    private String groupNum;

    public static OrderResponseDTO fromEntity(TableOrderEntity entity) {
        return OrderResponseDTO.builder()
                .orderId(entity.getOrderId())
                .menuName(entity.getMenu().getMenuName())
                .quantity(entity.getQuantity())
                .totalPrice(entity.getTotalPrice())
                .orderTime(entity.getOrderTime())
                .status(entity.getStatus())
                .tableId(entity.getDiningTable().getDiningTableId())
                .groupNum(entity.getGroupNum())
                .build();
    }
}
