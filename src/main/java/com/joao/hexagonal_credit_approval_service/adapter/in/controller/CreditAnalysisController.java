package com.joao.hexagonal_credit_approval_service.adapter.in.controller;

import com.joao.hexagonal_credit_approval_service.adapter.in.dto.response.CreditAnalysisByCustomerIdResponseDTO;
import com.joao.hexagonal_credit_approval_service.adapter.in.mapper.CreditAnalysisMapper;
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import com.joao.hexagonal_credit_approval_service.application.ports.in.GetAllCreditAnalysisByCustomerInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/credit-analysis")
@RequiredArgsConstructor
public class CreditAnalysisController {

    private final GetAllCreditAnalysisByCustomerInputPort getAllValidateCreditAnalysisByCustomerInputPort;
    private final CreditAnalysisMapper creditAnalysisMapper;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CreditAnalysisByCustomerIdResponseDTO>> getAllCreditAnalysisByCustomerId(@PathVariable UUID customerId) {

        List<CreditAnalysis> creditAnalysisList = getAllValidateCreditAnalysisByCustomerInputPort.getAll(customerId);

        if (creditAnalysisList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CreditAnalysisByCustomerIdResponseDTO> response = creditAnalysisList.stream()
                .map(creditAnalysisMapper::toCreditAnalysisByCustomerIdResponseDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

}
