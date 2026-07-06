package com.joao.hexagonal_credit_approval_service.application.ports.in;

import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;

import java.util.List;
import java.util.UUID;

public interface GetAllCreditAnalysisByCustomerInputPort {
    List<CreditAnalysis> getAll(UUID customerId);
}
