package com.reactive.webflux2.scheduledtasks;

import com.reactive.webflux2.RecordProcessStatus;
import com.reactive.webflux2.domain.CreditCard;
import com.reactive.webflux2.service.CreditCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Slf4j
public class CreditCardProcessor {
    @Autowired
    CreditCardService creditCardService;

    @Scheduled(fixedDelay = 3000)
    public void processCC() throws InterruptedException {
        log.info("Starting scheduled task...");
        Flux<CreditCard> notProcessedCards = creditCardService.findCreditCardsByProcessStatus(RecordProcessStatus.NOT_PROCESSED);
        List<CreditCard> lst = notProcessedCards.collectList().block();
        lst.forEach(cc -> {
            //cc.setProcessStatus("PROCESSED");
            creditCardService.updateCreditCardProcessStatus(cc.getId(), RecordProcessStatus.PROCESSED);
        });
        log.info("Processing done for " + notProcessedCards.count().block() + " CCs");
    }

}
