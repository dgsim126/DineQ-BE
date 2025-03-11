package com.dineq.dineqbe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "menu")
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    private String menuName;
    private Integer menuPrice;
    private boolean onSale = true;
    private Integer menuPriority;
    private String menuInfo;
    private String menuImage;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    public MenuEntity(Integer category, String menuName, Integer menuPrice, String menuInfo, Integer menuPriority, String menuImage, boolean onSale) {
        this.category = new CategoryEntity();
        this.category.setCategoryId(category);
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuInfo = menuInfo;
        this.menuPriority = menuPriority;
        this.menuImage = menuImage;
        this.onSale = onSale;
    }
}
