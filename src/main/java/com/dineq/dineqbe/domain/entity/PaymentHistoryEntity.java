package com.dineq.dineqbe.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_history")
public class PaymentHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentHistoryId;

    private Integer orderId;
    private String menuName;
    private Integer diningTableId;
    private Integer quantity;
    private Integer totalPrice;
    private LocalDate paymentTime;
    private LocalDate createdAt;
    private String groupNum;
}
