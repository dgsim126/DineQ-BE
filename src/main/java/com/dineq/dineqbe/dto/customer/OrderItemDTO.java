package com.dineq.dineqbe.dto.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemDTO {
    private Long menuId;
    private Integer quantity;
}
