package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.MenuEntity;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
}
