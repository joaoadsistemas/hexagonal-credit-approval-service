package com.joao.hexagonal_credit_approval_service.adapter.in.mapper;

import com.joao.hexagonal_credit_approval_service.adapter.in.dto.response.CreditAnalysisByCustomerIdResponseDTO;
import com.joao.hexagonal_credit_approval_service.adapter.in.message.CreditAnalysisMessage;
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditAnalysisMapper {
    CreditAnalysis toCreditAnalysis(CreditAnalysisMessage creditAnalysisMessage);
    CreditAnalysisByCustomerIdResponseDTO toCreditAnalysisByCustomerIdResponseDTO(CreditAnalysis creditAnalysis);
}
