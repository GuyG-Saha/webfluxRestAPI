package com.reactive.webflux2.controller;

import com.reactive.webflux2.domain.CreditCard;
import com.reactive.webflux2.service.CreditCardService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditCard> saveCreditCard(@RequestBody @Valid CreditCard creditCard) {
        log.info("CreditCard: " + creditCard);
        return creditCardService.addCreditCard(creditCard);
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CreditCard> getAllCreditCards() {
        log.info("Retrieving all credit cards from DB!");
        return creditCardService.getAllCreditCards();
    }
}
