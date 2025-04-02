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
public class MenuListResponseDTO {
    private Long menuId;
    private String menuName;
    private Integer menuPrice;
    private Boolean onSale;
    private String menuImage;

    private Long categoryId;
    private String categoryName;
    private Integer categoryPriority;

    public static MenuListResponseDTO fromEntity(MenuEntity menu) {
        return MenuListResponseDTO.builder()
                .menuId(menu.getMenuId())
                .menuName(menu.getMenuName())
                .menuPrice(menu.getMenuPrice())
                .onSale(menu.getOnSale())
                .menuImage(menu.getMenuImage())
                .categoryId(menu.getCategory().getCategoryId())
                .categoryName(menu.getCategory().getCategoryName())
                .categoryPriority(menu.getCategory().getCategoryPriority())
                .build();
    }
}
