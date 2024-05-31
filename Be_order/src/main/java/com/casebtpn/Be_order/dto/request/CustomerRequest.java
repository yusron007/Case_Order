package com.casebtpn.Be_order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CustomerRequest {

    @NotBlank(message = "Nama Customer Tidak Boleh Kosong")
    private String customerName;

    @NotBlank(message = "Alamat Customer Tidak Boleh Kosong")
    private String customerAddress;

    @NotBlank(message = "No Telepon Customer Tidak Boleh Kosong")
    @Size(min = 8, max = 15, message = "No Telepon harus antara 8 hingga 15 karakter")
    @Pattern(regexp = "^\\d+$", message = "No Telepon hanya boleh mengandung angka")
    private String customerPhone;

    private MultipartFile pic;
}
