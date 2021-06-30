/**
 * 
 */
package com.example.demo.controller;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MyController {

	@Value("${apiKey}")
	private String apiKey;
	@Value("${fromEnv:garbage}")
	private String fromEnv;
	@Value("${toBeOverriden}")
	private String toBeOverriden;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CassandraTemplate cassandraTemplate;

	@PostConstruct
	public void init() {
		// https://www.baeldung.com/spring-data-cassandra-tutorial
		final UUID timeBased = Uuids.timeBased();
		final Book javaBook = new Book(timeBased, "Head First Java", "O'Reilly Media",
				ImmutableSet.of("Computer", "Software"));
		bookRepository.saveAll(ImmutableSet.of(javaBook));
		bookRepository.findAll().stream().forEach(System.out::println);

		final Iterable<Book> findByTitleAndPublisher = bookRepository.findByTitleAndPublisher("Head First Java",
				"O'Reilly Media");
		findByTitleAndPublisher.forEach(System.out::println);

		System.out.println("MyController.init():" + cassandraTemplate.insert(javaBook));

		final MapId mapId = new BasicMapId(
				ImmutableMap.of("id", timeBased, "title", "Head First Java", "publisher", "O'Reilly Media"));

		System.out.println(cassandraTemplate.exists(mapId, Book.class));
		
//		PrimaryKeyClass mapId2;
		

	}

	@Data
	@AllArgsConstructor
	public static class POJO {
		private final Properties properties;
		private final Map<String, String> env;
		private final String apiKey;
		private final String fromEnv;
		private final String toBeOverriden;
	}

	@GetMapping("/weather/1.0/forecast")
	@Valid
	public ResponseEntity<POJO> getWeatherPredictionByLocation() {

		final POJO body = new POJO(System.getProperties(), System.getenv(), apiKey, fromEnv, toBeOverriden);
		log.debug("body: {}", body);
		return ResponseEntity.ok(body);
	}
}
