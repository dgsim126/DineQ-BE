package com.dineq.dineqbe.dto.customer;

import com.dineq.dineqbe.domain.entity.MenuEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponseDTO {
    private Long menuId;
    private String menuName;
    private Integer menuPrice;
    private Boolean onSale;
    private Integer menuPriority;
    private String menuInfo;
    private String menuImage;

    private Long categoryId;
    private String categoryName;
    private Integer categoryPriority;

    public static MenuResponseDTO fromEntity(MenuEntity menu) {
        return MenuResponseDTO.builder()
                .menuId(menu.getMenuId())
                .menuName(menu.getMenuName())
                .menuPrice(menu.getMenuPrice())
                .onSale(menu.isOnSale())
                .menuInfo(menu.getMenuInfo())
                .menuImage(menu.getMenuImage())
                .categoryId(menu.getCategory().getCategoryId())
                .categoryName(menu.getCategory().getCategoryName())
                .categoryPriority(menu.getCategory().getCategoryPriority())
                .build();
    }
}
