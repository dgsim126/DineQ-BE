package com.dineq.dineqbe.domain.entity;

import com.dineq.dineqbe.domain.enums.UserType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String username;
    private String password;
    private LocalDate createdAt;
}
