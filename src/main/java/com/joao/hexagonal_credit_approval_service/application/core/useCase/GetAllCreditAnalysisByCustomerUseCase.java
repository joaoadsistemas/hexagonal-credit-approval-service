package com.joao.hexagonal_credit_approval_service.application.core.useCase;

import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import com.joao.hexagonal_credit_approval_service.application.ports.in.GetAllCreditAnalysisByCustomerInputPort;
import com.joao.hexagonal_credit_approval_service.application.ports.out.GetAllAnalysisByCustomerOutputPort;

import java.util.List;
import java.util.UUID;

public class GetAllCreditAnalysisByCustomerUseCase implements GetAllCreditAnalysisByCustomerInputPort {

    private final GetAllAnalysisByCustomerOutputPort getAllAnalysisByCustomerOutputPort;

    public GetAllCreditAnalysisByCustomerUseCase(GetAllAnalysisByCustomerOutputPort getAllAnalysisByCustomerOutputPort) {
        this.getAllAnalysisByCustomerOutputPort = getAllAnalysisByCustomerOutputPort;
    }

    @Override
    public List<CreditAnalysis> getAll(UUID customerId) {
        return getAllAnalysisByCustomerOutputPort.getAll(customerId);
    }
}
