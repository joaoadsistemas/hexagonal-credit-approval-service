package com.joao.hexagonal_credit_approval_service.adapter.out;

import com.joao.hexagonal_credit_approval_service.adapter.out.exception.SerializeException;
import com.joao.hexagonal_credit_approval_service.adapter.out.mapper.CreditAnalysisResultMessageMapper;
import com.joao.hexagonal_credit_approval_service.adapter.out.message.CreditAnalysisResultMessage;
import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import com.joao.hexagonal_credit_approval_service.application.ports.out.NotifyCreditAnalysisOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class NotifyCreditAnalysisAdapter implements NotifyCreditAnalysisOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CreditAnalysisResultMessageMapper creditAnalysisResultMessageMapper;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "credit-analysis-result";

    @Override
    public void send(CreditAnalysis creditAnalysis) {
        CreditAnalysisResultMessage creditAnalysisResultMessage = creditAnalysisResultMessageMapper.toCreditAnalysisResultMessage(creditAnalysis);
        try {
            kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(creditAnalysisResultMessage));
        } catch (Exception e) {
            throw new SerializeException(creditAnalysisResultMessage);
        }
    }
}
