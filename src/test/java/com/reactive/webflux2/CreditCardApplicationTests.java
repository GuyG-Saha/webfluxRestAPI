package com.reactive.webflux2;

import com.reactive.webflux2.controller.CreditCardController;
import com.reactive.webflux2.domain.CreditCard;
import com.reactive.webflux2.service.CreditCardService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CreditCardController.class)
@AutoConfigureWebTestClient
class CreditCardApplicationTests {
	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private CreditCardService ccServiceMock;
	final static String BASE_URL = "/api/cc";
	static List<CreditCard> creditCards;
	@BeforeAll
	static void init() {
		creditCards = List.of(
				new CreditCard("123a", "123456******9901",
						"01", 2025, "PROCESSED"),
				new CreditCard("123b", "123456******9902",
						"04", 2024, "NOT_PROCESSED"),
				new CreditCard("123c", "123456******9903",
						"11", 2024, "NOT_PROCESSED"));
	}
	@Test
	void getAllCards() {
		when(ccServiceMock.getAllCreditCards()).thenReturn(Flux.fromIterable(creditCards));
		webTestClient
				.get()
				.uri(BASE_URL)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBodyList(CreditCard.class)
				.hasSize(3);
	}
	@Test
	void getOneProcessedCardByStatus() {
		RecordProcessStatus status = RecordProcessStatus.PROCESSED;
		var processedCard = creditCards.get(0);
		when(ccServiceMock.findCreditCardsByProcessStatus(status)).thenReturn(Flux.just(processedCard));
		webTestClient
				.get()
				.uri(BASE_URL + "/byStatus?status=PROCESSED")
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBodyList(CreditCard.class)
				.hasSize(1);
	}
	@Test
	void addNewCreditCard() {
		var newCreditCardInvalidMonth = new CreditCard("123d", "123456******9904",
				"12", 2023, "NOT_PROCESSED");
		when(ccServiceMock.addCreditCard(newCreditCardInvalidMonth)).thenReturn(Mono.just(newCreditCardInvalidMonth));
		webTestClient
				.post()
				.uri(BASE_URL + "/save")
				.bodyValue(newCreditCardInvalidMonth)
				.exchange()
				.expectStatus()
				.isCreated();
	}
	@Test
	void addNewInvalidExpMonthCreditCard() {
		var newCreditCardInvalidMonth = new CreditCard("123d", "123456******9904",
				"13", 2023, "NOT_PROCESSED");
		when(ccServiceMock.addCreditCard(newCreditCardInvalidMonth)).thenReturn(Mono.just(newCreditCardInvalidMonth));
		webTestClient
				.post()
				.uri(BASE_URL + "/save")
				.bodyValue(newCreditCardInvalidMonth)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody(String.class)
				.consumeWith(stringEntityExchangeResult -> {
					var response = stringEntityExchangeResult.getResponseBody();
					assert Objects.nonNull(response);
					assert Objects.equals(response, "Month must be between 01 and 12");
				});
	}
	@Test
	void addNewInvalidExpYearCreditCard() {
		var newCreditCardInvalidYear = new CreditCard("123d", "123456******9904",
				"12", -123, "NOT_PROCESSED");
		when(ccServiceMock.addCreditCard(newCreditCardInvalidYear)).thenReturn(Mono.just(newCreditCardInvalidYear));
		webTestClient
				.post()
				.uri(BASE_URL + "/save")
				.bodyValue(newCreditCardInvalidYear)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody(String.class)
				.consumeWith(stringEntityExchangeResult -> {
					var response = stringEntityExchangeResult.getResponseBody();
					assert Objects.nonNull(response);
					assert Objects.equals(response, "Earliest expiration year is 2023");
				});
	}
}