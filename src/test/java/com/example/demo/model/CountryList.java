package com.example.demo.model;

import java.util.List;

import lombok.Data;

@Data
public class CountryList {
	private List<Country> isoCodes;

	@Data
	public static class Country {
		private String name;
		private String alpha2;
		private String alpha3;
		private Long number;
		private String currency;		
	}

}
