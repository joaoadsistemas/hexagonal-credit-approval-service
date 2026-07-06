package com.joao.hexagonal_credit_approval_service.config;

import com.joao.hexagonal_credit_approval_service.adapter.out.GetAllAnalysisByCustomerOutputPortAdapter;
import com.joao.hexagonal_credit_approval_service.application.core.useCase.GetAllCreditAnalysisByCustomerUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetAllCreditAnalysisByCustomerConfig {

    @Bean
    public GetAllCreditAnalysisByCustomerUseCase getAllCreditAnalysisByCustomerUseCase(GetAllAnalysisByCustomerOutputPortAdapter getAllAnalysisByCustomerIdAdapter) {
        return new GetAllCreditAnalysisByCustomerUseCase(getAllAnalysisByCustomerIdAdapter);
    }

}
