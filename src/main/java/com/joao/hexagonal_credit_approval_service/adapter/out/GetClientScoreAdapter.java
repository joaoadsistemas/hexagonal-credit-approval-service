package com.joao.hexagonal_credit_approval_service.adapter.out;

import com.joao.hexagonal_credit_approval_service.adapter.out.client.SerasaScoreFakeApiClient;
import com.joao.hexagonal_credit_approval_service.application.ports.out.GetClientScoreOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetClientScoreAdapter implements GetClientScoreOutputPort {
    @Override
    public Long getScore(UUID customerId) {
        return SerasaScoreFakeApiClient.getScore(customerId);
    }
}
