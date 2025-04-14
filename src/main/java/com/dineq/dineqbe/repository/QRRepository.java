package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.QREntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRRepository extends JpaRepository<QREntity, Long> {
}
