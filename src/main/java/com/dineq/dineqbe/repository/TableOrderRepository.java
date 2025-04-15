package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import com.dineq.dineqbe.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableOrderRepository extends JpaRepository<TableOrderEntity, Long> {

    List<TableOrderEntity> findByDiningTable_DiningTableId(Long diningTableId);

    List<TableOrderEntity> findByStatus(OrderStatus status);

    List<TableOrderEntity> findAllByGroupNum(String groupNum);

}
