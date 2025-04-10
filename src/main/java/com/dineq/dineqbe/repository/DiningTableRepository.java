package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.DiningTableEntity;
import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTableEntity, Long>  {

    @Query("SELECT COALESCE(MAX(d.tableNumber), 0) FROM DiningTableEntity d")
    Long findMaxTableNumber();

    @Query("SELECT d FROM DiningTableEntity d WHERE d.tableNumber = (SELECT MAX(d2.tableNumber) FROM DiningTableEntity d2)")
    DiningTableEntity findTopByMaxTableNumber();

}
