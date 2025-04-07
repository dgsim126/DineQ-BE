package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.DiningTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTableEntity, Long>  {
}
