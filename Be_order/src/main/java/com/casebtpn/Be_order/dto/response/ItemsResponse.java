package com.casebtpn.Be_order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemsResponse {
    private Integer itemsId;
    private String itemsName;
    private String itemsCode;
    private Integer stock;
    private Integer price;
    private Boolean isAvailable;
    private String lastReStock;
}
