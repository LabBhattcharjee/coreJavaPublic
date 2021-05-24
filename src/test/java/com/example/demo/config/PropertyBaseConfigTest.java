package com.example.demo.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:config/app.properties")
public class PropertyBaseConfigTest extends BaseConfigTest {

}
