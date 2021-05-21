package com.example.demo.service;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.example.demo.model.CountryList;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

//@Service
//@Configuration
//@Data
public class TestServiceTest {

	@Test
	public void testService() throws JsonParseException, JsonMappingException, IOException {

		final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		final CountryList countryList = mapper.readValue(getClass().getResource("/iso3166.yml"), CountryList.class);
		System.out.println(countryList);
	}

}
