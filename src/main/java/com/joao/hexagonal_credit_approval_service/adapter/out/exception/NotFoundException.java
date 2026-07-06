package com.joao.hexagonal_credit_approval_service.adapter.out.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, UUID id) {
        super(message + ": " + id);
    }

}
