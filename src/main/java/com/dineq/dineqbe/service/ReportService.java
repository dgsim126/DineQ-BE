package com.dineq.dineqbe.service;

import com.dineq.dineqbe.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PaymentHistoryRepository paymentHistoryRepository;

    /**
     * 기간 별 총 매출 조회
     * [GET] /api/v1/store/reports/sales?startDate={yyyy-mm-dd}&endDate={yyyy-mm-dd}
     * @param startDate
     * @param endDate
     * @return
     */
    public int getTotalSales(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        Integer total = paymentHistoryRepository.findTotalSalesBetween(start, end);
        return total != null ? total : 0;
    }
}
