package com.dineq.dineqbe.dto.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TableOrderRequestDTO {
    private Long tableId;
    private List<OrderItemDTO> orders;
}
