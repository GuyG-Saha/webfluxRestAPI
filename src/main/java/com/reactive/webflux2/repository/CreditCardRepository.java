package com.reactive.webflux2.repository;

import com.reactive.webflux2.domain.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
}
