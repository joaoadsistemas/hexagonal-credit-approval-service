package com.joao.hexagonal_credit_approval_service.adapter.out;

import com.joao.hexagonal_credit_approval_service.adapter.out.repository.CreditAnalysisRepository;
import com.joao.hexagonal_credit_approval_service.application.core.domain.Status;
import com.joao.hexagonal_credit_approval_service.application.ports.out.CheckRecentCreditAnalysisOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckRecentCreditAnalysisAdapter implements CheckRecentCreditAnalysisOutputPort {

    private final CreditAnalysisRepository creditAnalysisRepository;

    @Override
    public boolean hasRecent(UUID customerId) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        return creditAnalysisRepository.findAllByCustomerIdOrderByAnalysisDateDesc(customerId).stream()
                .anyMatch(creditAnalysis -> creditAnalysis.getStatus() == Status.DENIED
                        && creditAnalysis.getAnalysisDate() != null
                        && creditAnalysis.getAnalysisDate().isAfter(cutoff));
    }
}
