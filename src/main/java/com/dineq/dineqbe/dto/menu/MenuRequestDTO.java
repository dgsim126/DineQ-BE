package com.dineq.dineqbe.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDTO {
    private String menuName;
    private Integer menuPrice;
    private Boolean onSale;
    private Integer menuPriority;
    private String menuInfo;
    private String menuImage;
    private Long categoryId;
}
