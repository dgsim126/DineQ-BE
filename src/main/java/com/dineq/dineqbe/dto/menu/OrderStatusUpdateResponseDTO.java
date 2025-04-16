package com.dineq.dineqbe.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateResponseDTO {

    private List<Long> success;
    private List<FailedOrderDTO> failed;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FailedOrderDTO {
        private Long orderId;
        private String reason;
    }

}
