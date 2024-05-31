package com.casebtpn.Be_order.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "items")
public class ItemsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "items_id")
    private Integer itemsId;

    @Column(name = "items_name")
    private String itemsName;

    @Column(name = "items_code")
    private String itemsCode;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "price")
    private Integer price;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "last_re_stock")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastReStock;

    @PrePersist
    public void prePersist() {
        this.lastReStock = LocalDateTime.now();
    }
}
