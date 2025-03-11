package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    boolean existsByCategoryName(String categoryName);
}
