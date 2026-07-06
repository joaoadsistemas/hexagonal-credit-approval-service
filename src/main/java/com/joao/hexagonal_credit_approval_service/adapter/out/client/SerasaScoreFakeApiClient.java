package com.joao.hexagonal_credit_approval_service.adapter.out.client;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SerasaScoreFakeApiClient {

    public static Long getScore(UUID customerId) {
        return ThreadLocalRandom.current().nextLong(0, 1000);
    }
}
