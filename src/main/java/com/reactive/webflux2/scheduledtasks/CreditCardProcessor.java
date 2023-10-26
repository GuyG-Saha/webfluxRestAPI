package com.reactive.webflux2.scheduledtasks;

import com.reactive.webflux2.RecordProcessStatus;
import com.reactive.webflux2.service.CreditCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreditCardProcessor {
    @Autowired
    CreditCardService creditCardService;
    @Scheduled(fixedDelay = 3000)
    public void processCC() throws InterruptedException {
        log.info("Starting scheduled task...");
        creditCardService.findCreditCardsByProcessStatus(RecordProcessStatus.NOT_PROCESSED)
                .flatMap(cc -> {
                    // Update the record in a non-blocking way
                    return creditCardService.updateCreditCardProcessStatus(cc.getId(), RecordProcessStatus.PROCESSED);
                })
                .subscribe();
        log.info("Finished processing request");
    }

}
