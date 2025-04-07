package com.dineq.dineqbe.dto.customer;

import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableOrderResponseDTO {
    private LocalDateTime orderTime;
    private Integer quantity;
    private String status;
    private Integer menuPrice;
    private Integer totalPrice;

    private String menuName;
    private String categoryName;

    public static TableOrderResponseDTO fromEntity(TableOrderEntity order) {
        return TableOrderResponseDTO.builder()
                .orderTime(order.getOrderTime())
                .quantity(order.getQuantity())
                .status(order.getStatus().name())
                .menuPrice(order.getMenu().getMenuPrice())
                .totalPrice(order.getTotalPrice())
                .menuName(order.getMenu().getMenuName())
                .categoryName(order.getMenu().getCategory().getCategoryName())
                .build();
    }
}
