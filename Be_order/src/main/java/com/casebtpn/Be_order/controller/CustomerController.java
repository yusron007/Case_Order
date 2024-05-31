package com.casebtpn.Be_order.controller;

import com.casebtpn.Be_order.dto.request.CustomerRequest;
import com.casebtpn.Be_order.service.CustomerService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<Object> addCustomer(@ModelAttribute CustomerRequest customerRequest){
        return customerService.addCustomer(customerRequest);
    }

    @GetMapping("/getCustomer")
    public ResponseEntity<Object> getCustomers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(required = false) String search) {
        return customerService.getCustomer(page, size, search);
    }

    @GetMapping("/getCustomer/{customerId}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Integer customerId){
        return customerService.getCustomerById(customerId);
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Integer customerId, @ModelAttribute CustomerRequest customerRequest){
        return customerService.updateCustomer(customerId, customerRequest);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Integer customerId){
        return customerService.deleteCustomer(customerId);
    }

    @GetMapping("/customerName")
    public ResponseEntity<Object> getCustomerName(@RequestParam(required = false) String search) {
        return customerService.getName(search);
    }

}
