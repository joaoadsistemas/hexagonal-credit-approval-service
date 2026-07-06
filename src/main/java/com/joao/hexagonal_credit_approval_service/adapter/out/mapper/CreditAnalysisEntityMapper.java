package com.joao.hexagonal_credit_approval_service.adapter.out.mapper;

import com.joao.hexagonal_credit_approval_service.adapter.out.entity.CreditAnalysisEntity;
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditAnalysisEntityMapper {

    CreditAnalysisEntity toCreditAnalysisEntity(CreditAnalysis creditAnalysis);
    CreditAnalysis toCreditAnalysis(CreditAnalysisEntity creditAnalysisEntity);

}
