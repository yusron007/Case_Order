package com.casebtpn.Be_order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank(message = "Customer Tidak Boleh Kosong")
    private Integer customerId;

    @NotBlank(message = "Items Tidak Boleh Kosong")
    private Integer itemsId;

    @NotBlank(message = "Quantity Tidak Boleh Kosong")
    private Integer quantity;
}
