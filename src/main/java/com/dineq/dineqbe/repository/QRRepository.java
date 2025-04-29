package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.QREntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface QRRepository extends JpaRepository<QREntity, Long> {
    boolean existsByTokenAndTableId(String token, String tableId);

    void deleteByCreatedAtBefore(LocalDateTime now);

    void deleteByTableId(String tableId);
}
