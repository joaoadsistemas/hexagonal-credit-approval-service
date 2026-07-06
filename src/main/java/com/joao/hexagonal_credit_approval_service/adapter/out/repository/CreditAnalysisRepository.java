package com.joao.hexagonal_credit_approval_service.adapter.out.repository;

import com.joao.hexagonal_credit_approval_service.adapter.out.entity.CreditAnalysisEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditAnalysisRepository extends MongoRepository<CreditAnalysisEntity, UUID> {

    Optional<CreditAnalysisEntity> findByCustomerId(UUID customerId);
    List<CreditAnalysisEntity> findAllByCustomerIdOrderByAnalysisDateDesc(UUID customerId);
}
