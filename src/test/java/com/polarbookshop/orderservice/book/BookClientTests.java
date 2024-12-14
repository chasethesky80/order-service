package com.polarbookshop.orderservice.book;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

class BookClientTests {

	private MockWebServer mockWebServer;
	private BookClient bookClient;

	@BeforeEach
	void setup() throws IOException {
		this.mockWebServer = new MockWebServer();
		this.mockWebServer.start();
		var webClient = WebClient.builder()
			.baseUrl(mockWebServer.url("/").uri().toString())
			.build();
		this.bookClient = new BookClient(webClient);
	}

	@AfterEach
	void clean() throws IOException {
		this.mockWebServer.shutdown();
	}

	@Test
	void whenBookExistsThenReturnBook() throws JsonProcessingException {
		var bookIsbn = "1234567890";
		var mockBook = new Book(bookIsbn, "Title", "Author", 9.90);
		var objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		var mockResponse = new MockResponse()
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.setBody(objectMapper.writeValueAsString(mockBook));
		mockWebServer.enqueue(mockResponse);
		final Mono<Book> result = bookClient.getBookByIsbn(bookIsbn);
		StepVerifier.create(result)
				.expectNextMatches(b -> b.isbn().equals(bookIsbn))
				.verifyComplete();
	}

}