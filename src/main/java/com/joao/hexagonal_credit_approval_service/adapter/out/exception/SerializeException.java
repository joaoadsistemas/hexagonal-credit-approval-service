package com.joao.hexagonal_credit_approval_service.adapter.out.exception;

import com.joao.hexagonal_credit_approval_service.adapter.out.message.CreditAnalysisResultMessage;

import java.util.UUID;

public class SerializeException extends RuntimeException {
    public SerializeException(CreditAnalysisResultMessage creditAnalysisResultMessage) {
        super("Error to serialize the object: " + creditAnalysisResultMessage.toString());
    }

}
