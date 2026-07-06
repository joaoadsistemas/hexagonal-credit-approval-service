package com.joao.hexagonal_credit_approval_service.adapter.out;

import com.joao.hexagonal_credit_approval_service.adapter.out.entity.CreditAnalysisEntity;
import com.joao.hexagonal_credit_approval_service.adapter.out.mapper.CreditAnalysisEntityMapper;
import com.joao.hexagonal_credit_approval_service.adapter.out.repository.CreditAnalysisRepository;
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import com.joao.hexagonal_credit_approval_service.application.ports.out.SaveCreditAnalysisOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveCreditAnalysisAdapter implements SaveCreditAnalysisOutputPort {

    private final CreditAnalysisEntityMapper creditAnalysisMapper;
    private final CreditAnalysisRepository creditAnalysisRepository;

    @Override
    public void save(CreditAnalysis creditAnalysis) {
        CreditAnalysisEntity creditAnalysisEntity = creditAnalysisMapper.toCreditAnalysisEntity(creditAnalysis);
        creditAnalysisRepository.save(creditAnalysisEntity);
    }
}
