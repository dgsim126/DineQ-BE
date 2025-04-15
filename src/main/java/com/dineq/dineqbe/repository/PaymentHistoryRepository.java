package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.PaymentHistoryEntity;
import com.dineq.dineqbe.dto.report.MenuSalesReportResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistoryEntity, Long> {

    // 매출 및 통계

    // 기간 별 총 매출 조회
    @Query("SELECT SUM(p.totalPrice) FROM PaymentHistoryEntity p WHERE p.paymentTime BETWEEN :start AND :end")
    Integer findTotalSalesBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // 기간 별 판매한 메뉴 수 & 각 메뉴의 매출 조회
    @Query("""
        SELECT new com.dineq.dineqbe.dto.report.MenuSalesReportResponseDTO(
            p.menuName,
            SUM(p.quantity),
            SUM(p.totalPrice)
        )
        FROM PaymentHistoryEntity p
        WHERE p.paymentTime BETWEEN :start AND :end
        GROUP BY p.menuName
        ORDER BY SUM(p.totalPrice) DESC
    """)
    List<MenuSalesReportResponseDTO> findMenuSalesBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
