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
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer diningTableId;

    @OneToMany(mappedBy = "diningTable", cascade = CascadeType.ALL)
    private List<TableOrder> tableOrders;
}
