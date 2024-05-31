package com.casebtpn.Be_order.repository;

import com.casebtpn.Be_order.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Integer> {
    Page<OrderModel> findAll(Specification<OrderModel> spec, Pageable pageable);
}
