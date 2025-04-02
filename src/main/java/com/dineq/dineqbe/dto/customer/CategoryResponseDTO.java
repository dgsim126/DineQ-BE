package com.dineq.dineqbe.dto.customer;
import com.dineq.dineqbe.domain.entity.CategoryEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDTO {
    private Long categoryId;
    private String categoryName;

    public static CategoryResponseDTO fromEntity(CategoryEntity category) {
        return CategoryResponseDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }
}