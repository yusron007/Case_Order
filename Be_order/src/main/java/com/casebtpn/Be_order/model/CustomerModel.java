package com.casebtpn.Be_order.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "customers")
public class CustomerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "pic")
    private String pic;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastOrderDate;

    @PrePersist
    public void prePersist() {
        this.lastOrderDate = LocalDateTime.now();
    }

}
