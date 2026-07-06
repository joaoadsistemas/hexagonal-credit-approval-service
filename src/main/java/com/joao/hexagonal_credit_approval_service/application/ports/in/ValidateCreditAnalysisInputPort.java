package com.joao.hexagonal_credit_approval_service.application.ports.in;

import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;

public interface ValidateCreditAnalysisInputPort {
    CreditAnalysis validate(CreditAnalysis creditAnalysis);
}
