package com.casebtpn.Be_order.dto.response;

import com.casebtpn.Be_order.model.CustomerModel;
import com.casebtpn.Be_order.model.ItemsModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private Integer orderId;
    private CustomerModel customer;
    private ItemsModel items;
    private String orderCode;
    private Integer quantity;
    private Integer totalPrice;
    private String orderDate;
}
