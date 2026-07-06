package com.joao.hexagonal_credit_approval_service.adapter.out;

import com.joao.hexagonal_credit_approval_service.adapter.out.entity.CreditAnalysisEntity;
import com.joao.hexagonal_credit_approval_service.adapter.out.mapper.CreditAnalysisEntityMapper;
import com.joao.hexagonal_credit_approval_service.adapter.out.repository.CreditAnalysisRepository;
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import com.joao.hexagonal_credit_approval_service.application.ports.out.GetAllAnalysisByCustomerOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAllAnalysisByCustomerOutputPortAdapter implements GetAllAnalysisByCustomerOutputPort {

    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CreditAnalysisEntityMapper creditAnalysisEntityMapper;


    @Override
    public List<CreditAnalysis> getAll(UUID customerId) {
        List<CreditAnalysisEntity> creditAnalysisEntityList = creditAnalysisRepository.findAllByCustomerIdOrderByAnalysisDateDesc(customerId);
        return creditAnalysisEntityList.stream()
                .map(creditAnalysisEntityMapper::toCreditAnalysis)
                .toList();
    }
}
