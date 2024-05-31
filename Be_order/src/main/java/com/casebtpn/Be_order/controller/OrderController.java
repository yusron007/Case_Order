package com.casebtpn.Be_order.controller;

import com.casebtpn.Be_order.dto.request.OrderRequest;
import com.casebtpn.Be_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/create")
    public ResponseEntity<Object> addOorder(@RequestBody OrderRequest orderRequest) {
        return orderService.addOrder(orderRequest);
    }

    @GetMapping("/order/get")
    public ResponseEntity<Object> getOrder(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) String search) {
        return orderService.getOrder(page, size, search);
    }

    @GetMapping("/order/getById/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable Integer orderId){
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/order/update/{orderId}")
    public ResponseEntity<Object> updateOrder(@PathVariable Integer orderId, @RequestBody OrderRequest orderRequest) {
        return orderService.updateOrder(orderId, orderRequest);
    }

    @DeleteMapping("/order/delete/{orderId}")
    public ResponseEntity<Object> deleteItems(@PathVariable Integer orderId) {
        return orderService.deleteOrder(orderId);
    }
}
