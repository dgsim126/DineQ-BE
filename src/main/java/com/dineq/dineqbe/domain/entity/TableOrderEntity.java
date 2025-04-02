package com.dineq.dineqbe.domain.entity;

import com.dineq.dineqbe.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "table_order")
public class TableOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "dining_table_id", nullable = false)
    private DiningTableEntity diningTable;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;

    private Integer quantity;
    private Integer totalPrice;
    private LocalDate paymentTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String groupNum;
}
