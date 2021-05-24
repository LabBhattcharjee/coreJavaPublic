package com.example.demo.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:config/app2.yml")
public class YMLBaseConfigTest extends BaseConfigTest {

}
