package com.casebtpn.Be_order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse {
    private Integer customerId;
    private String customerName;
    private String customerAddress;
    private String customerCode;
    private String customerPhone;
    private String pic;
    private Boolean isActive;
    private String lastOrderDate;
}
