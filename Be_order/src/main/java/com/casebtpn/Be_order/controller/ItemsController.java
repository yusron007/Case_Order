package com.casebtpn.Be_order.controller;

import com.casebtpn.Be_order.dto.request.ItemsRequest;
import com.casebtpn.Be_order.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;

    @PostMapping("/items/create")
    public ResponseEntity<Object> addItems(@RequestBody ItemsRequest itemsRequest) {
        return itemsService.addItems(itemsRequest);
    }

    @GetMapping("/items/get")
    public ResponseEntity<Object> getItems(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) String search) {
        return itemsService.getitems(page, size, search);
    }

    @GetMapping("/items/getById/{itemsId}")
    public ResponseEntity<Object> getById(@PathVariable Integer itemsId) {
        return itemsService.geItemsById(itemsId);
    }

    @PutMapping("/items/update/{itemsId}")
    public ResponseEntity<Object> updateItems(@PathVariable Integer itemsId, @RequestBody ItemsRequest itemsRequest) {
        return itemsService.updateItems(itemsId, itemsRequest);
    }

    @DeleteMapping("/items/delete/{itemsId}")
    public ResponseEntity<Object> deleteItems(@PathVariable Integer itemsId) {
        return itemsService.deleteItems(itemsId);
    }

    @GetMapping("/items/getName")
    public ResponseEntity<Object> getItemsName(@RequestParam(required = false) String search) {
        return itemsService.getItemsName(search);
    }
}
