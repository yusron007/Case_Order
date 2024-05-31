package com.casebtpn.Be_order.repository;

import com.casebtpn.Be_order.model.CustomerModel;
import com.casebtpn.Be_order.model.ItemsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<ItemsModel, Integer> {
    List<ItemsModel> findByIsAvailableTrue();

    Page<ItemsModel> findAll(Specification<ItemsModel> spec, Pageable pageable);

    List<ItemsModel> findAll(Specification<ItemsModel> spec);
}
