package com.dineq.dineqbe.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuSalesReportResponseDTO {
    private String menuName;
    private Long totalSold;
    private Long totalRevenue;
}
