package com.joao.hexagonal_credit_approval_service.adapter.out.mapper;

import com.joao.hexagonal_credit_approval_service.adapter.out.message.CreditAnalysisResultMessage;
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditAnalysisResultMessageMapper {
    CreditAnalysisResultMessage toCreditAnalysisResultMessage(CreditAnalysis creditAnalysis);
}
