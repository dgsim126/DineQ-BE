package com.dineq.dineqbe.dto.menu;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderStatusUpdateRequestDTO {
    private List<Long> orderId;
}
