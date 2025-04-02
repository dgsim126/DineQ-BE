package com.dineq.dineqbe.dto.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryPriorityUpdateRequestDTO {
    private List<CategoryPriorityRequestDTO> priorities;
}
