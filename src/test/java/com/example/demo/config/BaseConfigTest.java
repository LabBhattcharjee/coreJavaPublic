package com.example.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//https://mkyong.com/spring-boot/spring-boot-configurationproperties-example/
class BaseConfigTest {

	@Autowired
	private AppProperties appProperties;

	@Test
	public void test() {
		System.out.println("BaseConfigTest.test():" + appProperties);
	}
}
