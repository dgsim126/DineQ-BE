package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableOrderRepository extends JpaRepository<TableOrderEntity, Integer> {
}
