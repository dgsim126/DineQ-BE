package com.dineq.dineqbe.dto.table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ChangeTableRequestDTO {
    private Long fromTableId;
    private Long toTableId;
}
