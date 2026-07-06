package com.joao.hexagonal_credit_approval_service.config;

import com.joao.hexagonal_credit_approval_service.adapter.out.CheckRecentCreditAnalysisAdapter;
import com.joao.hexagonal_credit_approval_service.adapter.out.GetClientScoreAdapter;
import com.joao.hexagonal_credit_approval_service.adapter.out.NotifyCreditAnalysisAdapter;
import com.joao.hexagonal_credit_approval_service.adapter.out.SaveCreditAnalysisAdapter;
import com.joao.hexagonal_credit_approval_service.application.core.useCase.ValidateCreditAnalysisUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCreditAnalysisConfig {

    @Bean
    public ValidateCreditAnalysisUseCase creditAnalysisUseCase(
            CheckRecentCreditAnalysisAdapter checkRecentCreditAnalysisAdapter,
            GetClientScoreAdapter getClientScoreAdapter,
            SaveCreditAnalysisAdapter saveCreditAnalysisAdapter,
            NotifyCreditAnalysisAdapter notifyCreditAnalysisAdapter
    ) {
        return new ValidateCreditAnalysisUseCase(
                checkRecentCreditAnalysisAdapter,
                getClientScoreAdapter,
                saveCreditAnalysisAdapter,
                notifyCreditAnalysisAdapter
        );
    }

}
