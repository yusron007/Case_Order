package com.casebtpn.Be_order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BodyResponse {
    private String message;
    private int statusCode;
    private String status;
    private Object data;
    private Map<String, Object> pagination;
}
