package com.casebtpn.Be_order.repository;

import com.casebtpn.Be_order.model.CustomerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Integer> {

    Page<CustomerModel> findAll(Specification<CustomerModel> spec, Pageable pageable);

    List<CustomerModel> findAll(Specification<CustomerModel> spec);
}
