package com.dineq.dineqbe.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private Long orderId;
    private String menuName;
    private Long diningTableId;
    private Integer quantity;
    private Integer totalPrice;
    private LocalDateTime paymentTime;
    private LocalDateTime createdAt;
    private String groupNum;
}
