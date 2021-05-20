/**
 * 
 */
package com.example.demo.controller;

import java.util.Map;
import java.util.Properties;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
public class MyController {

	@Value("${apiKey}")
	private String apiKey;
	@Value("${fromEnv:garbage}")
	private String fromEnv;
	@Value("${toBeOverriden}")
	private String toBeOverriden;

	@Data
	@AllArgsConstructor
	public static class POJO {
		private final Properties properties;
		private final Map<String, String> env;
		private final String apiKey, fromEnv, toBeOverriden;
	}

	@GetMapping("/weather/1.0/forecast")
	@Valid
	public ResponseEntity<POJO> getWeatherPredictionByLocation() {

		return new ResponseEntity<>(
				new POJO(System.getProperties(), System.getenv(), apiKey, fromEnv, toBeOverriden), HttpStatus.OK);
	}
}
