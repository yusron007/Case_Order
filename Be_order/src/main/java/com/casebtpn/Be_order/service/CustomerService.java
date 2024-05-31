package com.casebtpn.Be_order.service;

import com.casebtpn.Be_order.dto.request.CustomerRequest;
import com.casebtpn.Be_order.dto.response.BodyResponse;
import com.casebtpn.Be_order.dto.response.CustomerResponse;
import com.casebtpn.Be_order.model.CustomerModel;
import com.casebtpn.Be_order.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MinioService minioService;

    @Transactional
    public ResponseEntity<Object> addCustomer(CustomerRequest customerRequest){
        try{
            CustomerModel customerModel = new CustomerModel();
            customerModel.setCustomerName(customerRequest.getCustomerName());
            customerModel.setCustomerCode("CUS-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
            customerModel.setCustomerAddress(customerRequest.getCustomerAddress());
            customerModel.setCustomerPhone(customerRequest.getCustomerPhone());

            // Unggah file gambar ke MinIO dan dapatkan nama file
            String fileName = minioService.uploadFile(customerRequest.getPic());
            customerModel.setPic(fileName);

            customerModel.setIsActive(true);
            customerRepository.save(customerModel);

            BodyResponse bodyResponse = BodyResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK.name())
                    .message("Add Customer Success")
                    .build();

            return ResponseEntity.ok().body(bodyResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while adding customer", e);
        }
    }

    public ResponseEntity<Object> getCustomer(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("customerName").ascending());

        Specification<CustomerModel> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive")));

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("customerName"), "%" + search + "%"));
        }

        Page<CustomerModel> customerPage = customerRepository.findAll(spec, pageable);

        List<CustomerResponse> responses = customerPage.stream()
                .map(customerModel -> {
                    CustomerResponse response = new CustomerResponse();
                    response.setCustomerId(customerModel.getCustomerId());
                    response.setCustomerName(customerModel.getCustomerName());
                    response.setCustomerAddress(customerModel.getCustomerAddress());
                    response.setCustomerCode(customerModel.getCustomerCode());
                    response.setCustomerPhone(customerModel.getCustomerPhone());
                    try {
                        response.setPic(minioService.getPresignedUrl(customerModel.getPic()));
                    } catch (Exception e) {
                        response.setPic(null);
                    }
                    response.setIsActive(customerModel.getIsActive());
                    response.setLastOrderDate(customerModel.getLastOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    return response;
                })
                .collect(Collectors.toList());

        Map<String, Object> paginationInfo = new HashMap<>();
        paginationInfo.put("totalPages", customerPage.getTotalPages());
        paginationInfo.put("totalElements", customerPage.getTotalElements());
        paginationInfo.put("currentPage", customerPage.getNumber());
        paginationInfo.put("pageSize", customerPage.getSize());

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Get Display Customer List")
                .data(responses)
                .pagination(paginationInfo)
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }

    public ResponseEntity<Object> getCustomerById (Integer customerId){
        CustomerModel customerModel = customerRepository.findById(customerId).orElse(null);

        if (customerModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        if (!customerModel.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.GONE, "Customer has already been deleted");
        }

        CustomerResponse response = new CustomerResponse();
        response.setCustomerName(customerModel.getCustomerName());
        response.setCustomerAddress(customerModel.getCustomerAddress());
        response.setCustomerCode(customerModel.getCustomerCode());
        response.setCustomerPhone(customerModel.getCustomerPhone());
        try {
            response.setPic(minioService.getPresignedUrl(customerModel.getPic()));
        } catch (Exception e) {
            response.setPic(null);
        }
        response.setIsActive(customerModel.getIsActive());
        response.setLastOrderDate(customerModel.getLastOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Get Display Customer List By Id")
                .data(response)
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }

    public ResponseEntity<Object> updateCustomer(Integer customerId, CustomerRequest customerRequest){
        try{
            CustomerModel customerModel = customerRepository.findById(customerId).orElse(null);

            if (customerModel == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
            }

            if (!customerModel.getIsActive()) {
                throw new ResponseStatusException(HttpStatus.GONE, "Customer has already been deleted");
            }

            customerModel.setCustomerName(customerRequest.getCustomerName());
            customerModel.setCustomerAddress(customerRequest.getCustomerAddress());
            customerModel.setCustomerPhone(customerRequest.getCustomerPhone());

            if (customerRequest.getPic() != null && !customerRequest.getPic().isEmpty()) {
                try {
                    // Hapus gambar lama dari MinIO
                    if (customerModel.getPic() != null) {
                        minioService.removeObject(customerModel.getPic());
                    }
                    // Upload gambar baru ke MinIO
                    String newPicFileName = minioService.uploadFile(customerRequest.getPic());
                    customerModel.setPic(newPicFileName);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading new picture", e);
                }
            }
            customerRepository.save(customerModel);

            BodyResponse bodyResponse = BodyResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK.name())
                    .message("Update Customer Success")
                    .build();

            return ResponseEntity.ok().body(bodyResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while adding customer", e);
        }
    }

    public ResponseEntity<Object> deleteCustomer(Integer customerId) {

        CustomerModel customerModel = customerRepository.findById(customerId).orElse(null);

        if (customerModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        if (!customerModel.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.GONE, "Customer has already been deleted");
        }

        customerModel.setIsActive(false);
        try {
            if (customerModel.getPic() != null) {
                minioService.removeObject(customerModel.getPic());
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting picture from MinIO", e);
        }
        customerRepository.save(customerModel);

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Delete Customer Success")
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }

    public ResponseEntity<Object> getName(String search) {

        Specification<CustomerModel> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive")));

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("customerName"), "%" + search + "%"));
        }

        List<CustomerModel> customerModels = customerRepository.findAll(spec);

        List<CustomerResponse> responses = customerModels.stream()
                .map(customerModel -> {
                    CustomerResponse response = new CustomerResponse();
                    response.setCustomerId(customerModel.getCustomerId());
                    response.setCustomerName(customerModel.getCustomerName());
                    return response;
                })
                .collect(Collectors.toList());

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Get Customer Name List")
                .data(responses)
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }
}
