package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.PaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistoryEntity, Long> {

    // 매출 및 통계
    @Query("SELECT SUM(p.totalPrice) FROM PaymentHistoryEntity p WHERE p.paymentTime BETWEEN :start AND :end")
    Integer findTotalSalesBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
