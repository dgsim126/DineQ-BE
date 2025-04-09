package com.dineq.dineqbe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dining_table")
public class DiningTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diningTableId;

    private Long tableNumber;

    @OneToMany(mappedBy = "diningTable", cascade = CascadeType.ALL)
    private List<TableOrderEntity> tableOrders;
}
