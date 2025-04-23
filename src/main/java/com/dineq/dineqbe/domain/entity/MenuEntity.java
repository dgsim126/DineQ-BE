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
    private Long menuId;

    private String menuName;
    private Integer menuPrice;

    @Column(nullable = false)
    @Builder.Default
    private Boolean onSale = true;

    @Column(unique = true)
    private Integer menuPriority;
    private String menuInfo;
    private String menuImage;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    public MenuEntity(CategoryEntity category, String menuName, Integer menuPrice, String menuInfo, Integer menuPriority, String menuImage, Boolean onSale) {
        this.category= category;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuInfo = menuInfo;
        this.menuPriority = menuPriority;
        this.menuImage = menuImage;
        this.onSale = onSale;
    }
}
