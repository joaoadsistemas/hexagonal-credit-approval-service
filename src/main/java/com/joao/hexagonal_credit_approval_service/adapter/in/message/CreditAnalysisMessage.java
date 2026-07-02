package com.joao.hexagonal_credit_approval_service.adapter.in.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditAnalysisMessage {

    private UUID analysisId;
    private UUID customerId;
    private BigDecimal declaredIncome;
    private Integer age;

}
