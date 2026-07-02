package com.joao.hexagonal_credit_approval_service.application.ports.out;

import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;

public interface SaveCreditAnalysisOutputPort {
    void save(CreditAnalysis creditAnalysis);
}
