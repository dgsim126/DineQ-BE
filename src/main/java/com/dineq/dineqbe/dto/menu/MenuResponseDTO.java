package com.dineq.dineqbe.dto.menu;

import com.dineq.dineqbe.domain.entity.MenuEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponseDTO {
    private Long menuId;
    private String menuName;
    private Integer menuPrice;
    private Boolean onSale;
    private Integer menuPriority;
    private String menuInfo;
    private String menuImage;
    private Long categoryId;
}
