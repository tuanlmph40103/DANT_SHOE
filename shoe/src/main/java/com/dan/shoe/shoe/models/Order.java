package com.dan.shoe.shoe.models;

import com.dan.shoe.shoe.models.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;
    LocalDateTime orderTime;
    int totalPrice;
    @Enumerated(EnumType.STRING)
    OrderStatus status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    Set<OrderItem> orderItems = new HashSet<>();
    boolean paid = false;
    double discountAmount;
    String discountDetails;
    LocalDateTime createdAt = LocalDateTime.now();
}

