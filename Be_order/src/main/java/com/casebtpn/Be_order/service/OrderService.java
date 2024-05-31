package com.casebtpn.Be_order.service;

import com.casebtpn.Be_order.dto.request.OrderRequest;
import com.casebtpn.Be_order.dto.response.BodyResponse;
import com.casebtpn.Be_order.dto.response.OrderResponse;
import com.casebtpn.Be_order.model.CustomerModel;
import com.casebtpn.Be_order.model.ItemsModel;
import com.casebtpn.Be_order.model.OrderModel;
import com.casebtpn.Be_order.repository.CustomerRepository;
import com.casebtpn.Be_order.repository.ItemsRepository;
import com.casebtpn.Be_order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Transactional
    public ResponseEntity<Object> addOrder(OrderRequest orderRequest){
        CustomerModel customerModel = customerRepository.findById(orderRequest.getCustomerId()).orElse(null);

        if (customerModel== null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        ItemsModel itemsModel = itemsRepository.findById(orderRequest.getItemsId()).orElse(null);

        if (itemsModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Items not found");
        }

        try{
            OrderModel orderModel = new OrderModel();
            orderModel.setCustomer(customerModel);
            orderModel.setItems(itemsModel);
            orderModel.setOrderCode("ORD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
            orderModel.setQuantity(orderRequest.getQuantity());
            orderModel.setTotalPrice(orderRequest.getQuantity() * itemsModel.getPrice());
            orderRepository.save(orderModel);

            BodyResponse bodyResponse = BodyResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK.name())
                    .message("Add Order Success")
                    .build();

            return ResponseEntity.ok().body(bodyResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while adding Order", e);
        }
    }

    public ResponseEntity<Object> getOrder(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").ascending());

        Specification<OrderModel> spec = Specification.where(null);
        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("customer").get("customerName"), "%" + search + "%"));
        }

        Page<OrderModel> orderPage = orderRepository.findAll(spec, pageable);

        List<OrderResponse> responses = orderPage.stream()
                .map(orderModel -> {
                    OrderResponse orderResponse = new OrderResponse();
                    orderResponse.setOrderId(orderModel.getOrderId());
                    orderResponse.setCustomer(orderModel.getCustomer());
                    orderResponse.setItems(orderModel.getItems());
                    orderResponse.setOrderCode(orderModel.getOrderCode());
                    orderResponse.setQuantity(orderModel.getQuantity());
                    orderResponse.setTotalPrice(orderModel.getTotalPrice());
                    orderResponse.setOrderDate(orderModel.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    return orderResponse;
                })
                .collect(Collectors.toList());

        Map<String, Object> paginationInfo = new HashMap<>();
        paginationInfo.put("totalPages", orderPage.getTotalPages());
        paginationInfo.put("totalElements", orderPage.getTotalElements());
        paginationInfo.put("currentPage", orderPage.getNumber());
        paginationInfo.put("pageSize", orderPage.getSize());

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Get Display Order List")
                .data(responses)
                .pagination(paginationInfo)
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }

    public ResponseEntity<Object> getOrderById (Integer orderId){
        OrderModel orderModel = orderRepository.findById(orderId).orElse(null);

        if (orderModel== null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setCustomer(orderModel.getCustomer());
        orderResponse.setItems(orderModel.getItems());
        orderResponse.setOrderCode(orderModel.getOrderCode());
        orderResponse.setQuantity(orderModel.getQuantity());
        orderResponse.setTotalPrice(orderModel.getTotalPrice());
        orderResponse.setOrderDate(orderModel.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Get Display Order List By Id")
                .data(orderResponse)
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }

    public ResponseEntity<Object> updateOrder(Integer orderId, OrderRequest orderRequest){
        try{
            OrderModel orderModel = orderRepository.findById(orderId).orElse(null);

            if (orderModel == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
            }

            CustomerModel customerModel = customerRepository.findById(orderRequest.getCustomerId()).orElse(null);

            if (customerModel== null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
            }

            ItemsModel itemsModel = itemsRepository.findById(orderRequest.getItemsId()).orElse(null);

            if (itemsModel == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Items not found");
            }


            orderModel.setCustomer(customerModel);
            orderModel.setItems(itemsModel);
            orderModel.setQuantity(orderRequest.getQuantity());
            orderModel.setTotalPrice(orderRequest.getQuantity() * itemsModel.getPrice());
            orderRepository.save(orderModel);

            BodyResponse bodyResponse = BodyResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK.name())
                    .message("Update Order Success")
                    .build();

            return ResponseEntity.ok().body(bodyResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while adding Order", e);
        }
    }

    public ResponseEntity<Object> deleteOrder(Integer orderId) {

        OrderModel orderModel = orderRepository.findById(orderId).orElse(null);

        if (orderModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        orderRepository.delete(orderModel);

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Delete Order Success")
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }
}
