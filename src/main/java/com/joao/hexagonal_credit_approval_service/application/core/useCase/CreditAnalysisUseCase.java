package com.joao.hexagonal_credit_approval_service.application.core.useCase;

import com.joao.hexagonal_credit_approval_service.application.core.domain.CreditAnalysis;
import com.joao.hexagonal_credit_approval_service.application.ports.in.CreditAnalysisInputPort;
import com.joao.hexagonal_credit_approval_service.application.ports.out.GetClientScoreOutputPort;
import com.joao.hexagonal_credit_approval_service.application.ports.out.CheckRecentCreditAnalysisOutputPort;
import com.joao.hexagonal_credit_approval_service.application.ports.out.SaveCreditAnalysisOutputPort;
import com.joao.hexagonal_credit_approval_service.application.ports.out.NotifyCreditAnalysisOutputPort;

public class CreditAnalysisUseCase implements CreditAnalysisInputPort {

    private final CheckRecentCreditAnalysisOutputPort checkRecentCreditAnalysisOutputPort;
    private final GetClientScoreOutputPort getClientScoreOutputPort;
    private final SaveCreditAnalysisOutputPort saveCreditAnalysisOutputPort;
    private final NotifyCreditAnalysisOutputPort notifyCreditAnalysisOutputPort;

    public CreditAnalysisUseCase(CheckRecentCreditAnalysisOutputPort checkRecentCreditAnalysisOutputPort,
                                 GetClientScoreOutputPort getClientScoreOutputPort,
                                 SaveCreditAnalysisOutputPort saveCreditAnalysisOutputPort,
                                 NotifyCreditAnalysisOutputPort notifyCreditAnalysisOutputPort) {
        this.checkRecentCreditAnalysisOutputPort = checkRecentCreditAnalysisOutputPort;
        this.getClientScoreOutputPort = getClientScoreOutputPort;
        this.saveCreditAnalysisOutputPort = saveCreditAnalysisOutputPort;
        this.notifyCreditAnalysisOutputPort = notifyCreditAnalysisOutputPort;
    }


    @Override
    public CreditAnalysis validate(CreditAnalysis creditAnalysis) {
        boolean hasRecent = checkRecentCreditAnalysisOutputPort.hasRecent(creditAnalysis.getCustomerId());
        Long score = getClientScoreOutputPort.getScore(creditAnalysis.getCustomerId());
        creditAnalysis.validate(hasRecent, score);
        saveCreditAnalysisOutputPort.save(creditAnalysis);
        notifyCreditAnalysisOutputPort.send(creditAnalysis);
        return creditAnalysis;
    }
}
