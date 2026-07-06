package com.joao.hexagonal_credit_approval_service.adapter.out.entity;

import com.joao.hexagonal_credit_approval_service.application.core.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "credit_analysis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditAnalysisEntity {
    @Id
    private UUID analysisId;
    private UUID customerId;
    private LocalDateTime analysisDate;
    private Status status;
    private BigDecimal approvedLimit = BigDecimal.ZERO;
    private String denialReason;
}
