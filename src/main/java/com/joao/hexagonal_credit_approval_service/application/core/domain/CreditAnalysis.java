package com.joao.hexagonal_credit_approval_service.application.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreditAnalysis {

    private UUID analysisId;
    private UUID customerId;
    private BigDecimal declaredIncome;
    private Integer age;
    private Status status;
    private String denialReason;
    private LocalDateTime analysisDate;
    private BigDecimal approvedLimit = BigDecimal.ZERO;

    public CreditAnalysis() {
    }

    public CreditAnalysis(UUID analysisId, UUID customerId, BigDecimal declaredIncome, Integer age, Status status, String denialReason, LocalDateTime analysisDate, BigDecimal approvedLimit) {
        this.analysisId = analysisId;
        this.customerId = customerId;
        this.declaredIncome = declaredIncome;
        this.age = age;
        this.status = status;
        this.denialReason = denialReason;
        this.analysisDate = analysisDate;
        this.approvedLimit = approvedLimit;
    }

    public void validate(boolean hasRecentCreditAnalysis, Long score) {
        if (hasRecentCreditAnalysis) {
         this.status = Status.DENIED;
         this.denialReason = "Recent credit analysis";
         return;
        }

        if (score >= 0 && score <= 399) {
            this.status = Status.DENIED;
            this.denialReason = "Credit score too low";
            return;
        }

        if (score >= 400 && score <= 699) {
            this.status = Status.APPROVED;
            this.approvedLimit = this.declaredIncome.multiply(new BigDecimal("0.30"));
        } else if (score >= 700 && score <= 1000) {
            this.status = Status.APPROVED;
            this.approvedLimit = this.declaredIncome.multiply(new BigDecimal("0.50"));
        }

        if (this.age < 21 || this.age > 65) {
            this.approvedLimit = this.approvedLimit.multiply(new BigDecimal("0.80"));
        }

        if (this.approvedLimit.compareTo(new BigDecimal("500.00")) < 0) {
            this.status = Status.DENIED;
            this.denialReason = "Credit value does not meet the operational requirements";
            this.approvedLimit = BigDecimal.ZERO;
        }
    }

    public UUID getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(UUID analysisId) {
        this.analysisId = analysisId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getDeclaredIncome() {
        return declaredIncome;
    }

    public void setDeclaredIncome(BigDecimal declaredIncome) {
        this.declaredIncome = declaredIncome;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDenialReason() {
        return denialReason;
    }

    public void setDenialReason(String denialReason) {
        this.denialReason = denialReason;
    }

    public LocalDateTime getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(LocalDateTime analysisDate) {
        this.analysisDate = analysisDate;
    }

    public BigDecimal getApprovedLimit() {
        return approvedLimit;
    }

    public void setApprovedLimit(BigDecimal approvedLimit) {
        this.approvedLimit = approvedLimit;
    }
}
