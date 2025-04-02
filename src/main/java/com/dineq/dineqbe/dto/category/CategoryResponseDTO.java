package com.dineq.dineqbe.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long categoryId;
    private String categoryName;
    private String categoryDesc;
    private Integer categoryPriority;
}
