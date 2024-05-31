package com.casebtpn.Be_order.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerModel customer;

    @ManyToOne
    @JoinColumn(name = "items_id")
    private ItemsModel items;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderDate;

    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();
    }
}
