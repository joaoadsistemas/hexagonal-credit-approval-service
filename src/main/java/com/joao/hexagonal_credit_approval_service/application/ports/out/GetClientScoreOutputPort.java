package com.joao.hexagonal_credit_approval_service.application.ports.out;
import java.util.UUID;

public interface GetClientScoreOutputPort {
    Long getScore(UUID customerId);
}
