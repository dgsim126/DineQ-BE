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
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    private String menuName;
    private Integer menuPrice;
    private Boolean onSale;
    private Integer menuPriority;
    private String menuInfo;
    private String menuImage;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
