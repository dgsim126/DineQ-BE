package com.dineq.dineqbe.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryPriorityRequestDTO {
    private Long categoryId;
    private Integer categoryPriority;
}
