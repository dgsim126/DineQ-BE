package com.dineq.dineqbe.dto.customer;

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
    private String categoryDesc;
}