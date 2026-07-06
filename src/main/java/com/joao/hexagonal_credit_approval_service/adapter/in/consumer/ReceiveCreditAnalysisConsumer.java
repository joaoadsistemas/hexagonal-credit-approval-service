package com.joao.hexagonal_credit_approval_service.adapter.in.consumer;

import com.joao.hexagonal_credit_approval_service.adapter.in.mapper.CreditAnalysisMapper;
import com.joao.hexagonal_credit_approval_service.adapter.in.message.CreditAnalysisMessage;
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import com.joao.hexagonal_credit_approval_service.application.ports.in.ValidateCreditAnalysisInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiveCreditAnalysisConsumer {

    private static final String TOPIC = "credit-analysis-requested";
    private final ValidateCreditAnalysisInputPort validateCreditAnalysisInputPort;
    private final CreditAnalysisMapper creditAnalysisMapper;

    @KafkaListener(topics = TOPIC, groupId = "credit-analysis-requested")
    public void receiveOrder(CreditAnalysisMessage creditAnalysisMessage) {
        CreditAnalysis creditAnalysis = creditAnalysisMapper.toCreditAnalysis(creditAnalysisMessage);
        validateCreditAnalysisInputPort.validate(creditAnalysis);
    }

}
