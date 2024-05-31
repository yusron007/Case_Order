package com.casebtpn.Be_order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItemsRequest {
    @NotBlank(message = "Nama Item Tidak Boleh Kosong")
    private String itemsName;

    @NotBlank(message = " Stock Tidak Boleh Kosong")
    private Integer stock;

    @NotBlank(message = "Harga Tidak Boleh Kosong")
    private Integer price;
}
