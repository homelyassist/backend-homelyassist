package com.homelyassist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

import static org.mockito.Mockito.mock;

@SpringBootTest
class HomelyAssistApplicationTests {

	@Test
	void contextLoads() {
	}

}