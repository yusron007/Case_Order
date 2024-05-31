package com.casebtpn.Be_order.service;

import com.casebtpn.Be_order.dto.request.CustomerRequest;
import com.casebtpn.Be_order.dto.request.ItemsRequest;
import com.casebtpn.Be_order.dto.response.BodyResponse;
import com.casebtpn.Be_order.dto.response.CustomerResponse;
import com.casebtpn.Be_order.dto.response.ItemsResponse;
import com.casebtpn.Be_order.dto.response.ItemsResponse;
import com.casebtpn.Be_order.model.CustomerModel;
import com.casebtpn.Be_order.model.ItemsModel;
import com.casebtpn.Be_order.repository.ItemsRepository;
import io.minio.messages.Item;
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
public class ItemsService {

    @Autowired
    private ItemsRepository itemsRepository;
    
    @Transactional
    public ResponseEntity<Object> addItems(ItemsRequest itemsRequest){
        try{
            ItemsModel itemsModel = new ItemsModel();
            itemsModel.setItemsName(itemsRequest.getItemsName());
            itemsModel.setItemsCode("ITM-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
            itemsModel.setPrice(itemsRequest.getPrice());
            itemsModel.setStock(itemsRequest.getStock());
            itemsModel.setIsAvailable(true);
            itemsRepository.save(itemsModel);

            BodyResponse bodyResponse = BodyResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK.name())
                    .message("Add Items Success")
                    .build();

            return ResponseEntity.ok().body(bodyResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while adding items", e);
        }
    }

    public ResponseEntity<Object> getitems(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("itemsName").ascending());

        Specification<ItemsModel> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isAvailable")));

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("itemsName"), "%" + search + "%"));
        }

        Page<ItemsModel> itemsPage = itemsRepository.findAll(spec, pageable);

        List<ItemsResponse> responses = itemsPage.stream()
                .map(itemsModel -> {
                    ItemsResponse response = new ItemsResponse();
                    response.setItemsId(itemsModel.getItemsId());
                    response.setItemsName(itemsModel.getItemsName());
                    response.setItemsCode(itemsModel.getItemsCode());
                    response.setStock(itemsModel.getStock());
                    response.setPrice(itemsModel.getPrice());
                    response.setIsAvailable(itemsModel.getIsAvailable());
                    response.setLastReStock(itemsModel.getLastReStock().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    return response;
                })
                .collect(Collectors.toList());

        Map<String, Object> paginationInfo = new HashMap<>();
        paginationInfo.put("totalPages", itemsPage.getTotalPages());
        paginationInfo.put("totalElements", itemsPage.getTotalElements());
        paginationInfo.put("currentPage", itemsPage.getNumber());
        paginationInfo.put("pageSize", itemsPage.getSize());

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Get Display Items List")
                .data(responses)
                .pagination(paginationInfo)
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }
    
    public ResponseEntity<Object> geItemsById (Integer itemsId){
        ItemsModel itemsModel = itemsRepository.findById(itemsId).orElse(null);

        if (itemsModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Items not found");
        }

        if (!itemsModel.getIsAvailable()) {
            throw new ResponseStatusException(HttpStatus.GONE, "Items has already been deleted");
        }

        ItemsResponse response = new ItemsResponse();
        response.setItemsName(itemsModel.getItemsName());
        response.setItemsCode(itemsModel.getItemsCode());
        response.setStock(itemsModel.getStock());
        response.setPrice(itemsModel.getPrice());
        response.setIsAvailable(itemsModel.getIsAvailable());
        response.setLastReStock(itemsModel.getLastReStock().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Get Display Items List By Id")
                .data(response)
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }

    public ResponseEntity<Object> updateItems(Integer itemsId, ItemsRequest itemsRequest){
        try{
            ItemsModel itemsModel = itemsRepository.findById(itemsId).orElse(null);

            if (itemsModel == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Items not found");
            }

            if (!itemsModel.getIsAvailable()) {
                throw new ResponseStatusException(HttpStatus.GONE, "Items has already been deleted");
            }

            itemsModel.setItemsName(itemsRequest.getItemsName());
            itemsModel.setStock(itemsRequest.getStock());
            itemsModel.setPrice(itemsRequest.getPrice());
            itemsRepository.save(itemsModel);

            BodyResponse bodyResponse = BodyResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK.name())
                    .message("Update Items Success")
                    .build();

            return ResponseEntity.ok().body(bodyResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while adding Items", e);
        }
    }

    public ResponseEntity<Object> deleteItems(Integer itemsId) {

        ItemsModel itemsModel = itemsRepository.findById(itemsId).orElse(null);

        if (itemsModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Items not found");
        }

        if (!itemsModel.getIsAvailable()) {
            throw new ResponseStatusException(HttpStatus.GONE, "Items has already been deleted");
        }

        itemsModel.setIsAvailable(false);
        itemsRepository.save(itemsModel);

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Delete Items Success")
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }

    public ResponseEntity<Object> getItemsName(String search) {

        Specification<ItemsModel> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isAvailable")));

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("itemsName"), "%" + search + "%"));
        }

        List<ItemsModel> itemsModels = itemsRepository.findAll(spec);

        List<ItemsResponse> responses = itemsModels.stream()
                .map(itemsModel -> {
                    ItemsResponse response = new ItemsResponse();
                    response.setItemsId(itemsModel.getItemsId());
                    response.setItemsName(itemsModel.getItemsName());
                    return response;
                })
                .collect(Collectors.toList());

        BodyResponse bodyResponse = BodyResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Get Items Name List")
                .data(responses)
                .build();

        return ResponseEntity.ok().body(bodyResponse);
    }
}
