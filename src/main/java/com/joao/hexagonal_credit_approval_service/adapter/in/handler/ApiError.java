package com.joao.hexagonal_credit_approval_service.adapter.in.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    private int code;
    private String status;
    private String path;
    private String timestamp;
}
