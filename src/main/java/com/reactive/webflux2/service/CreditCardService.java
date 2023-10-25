package com.reactive.webflux2.service;

import com.reactive.webflux2.RecordProcessStatus;
import com.reactive.webflux2.domain.CreditCard;
import com.reactive.webflux2.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;

    public Mono<CreditCard> addCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }
    public Flux<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll()
                .map(cc -> {
                    cc.setCCN(cc.toString());
                    return cc; });
    }
    public Flux<CreditCard> findCreditCardsByProcessStatus(RecordProcessStatus status) {
        return creditCardRepository
                .findByProcessStatus(status.name())
                .map(cc -> {
                    cc.setCCN(cc.toString());
                    return cc;
                });
    }
    public Flux<CreditCard> processCreditCards() {
        return creditCardRepository
                .findByProcessStatus("NOT_PROCESSED")
                .map(cc -> {
                    cc.setProcessStatus("PROCESSED");
                    creditCardRepository.save(cc);
                    return cc;
                });
    }
    public Mono<CreditCard> updateCreditCardProcessStatus(String id, RecordProcessStatus recordProcessStatus) {
        return creditCardRepository
                .findById(id)
                .flatMap(updatedCreditCard -> {
                    updatedCreditCard.setProcessStatus(recordProcessStatus.name());
                    return creditCardRepository.save(updatedCreditCard);
                });
    }
}
