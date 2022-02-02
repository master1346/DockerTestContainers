package com.example.spring_boot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    private static final GenericContainer<?> devapp = new GenericContainer<>("devapp")
		.withExposedPorts(8080);

	private static final GenericContainer<?> prodapp = new GenericContainer<>("prodapp")
			.withExposedPorts(8081);

	@Autowired
	TestRestTemplate restTemplate;

	@BeforeAll
	public static void setUp() {
     	devapp.start();
		prodapp.start();
	}

	@Test
	void contextLoads() {
		ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + devapp.getMappedPort(8080) + "/profile", String.class);
		System.out.println(forEntity.getBody());

		ResponseEntity<String> forEntity1 = restTemplate.getForEntity("http://localhost:" + prodapp.getMappedPort(8081) + "/profile", String.class);
		System.out.println(forEntity.getBody());
	}
	@Test
	void checkResponseDev() {
		ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + devapp.getMappedPort(8080) + "/profile", String.class);
		assertEquals(forEntity.getBody(), "Current profile is dev");
	}
	@Test
	void checkResponseProd() {
		ResponseEntity<String> forEntity1 = restTemplate.getForEntity("http://localhost:" + prodapp.getMappedPort(8081) + "/profile", String.class);
		assertEquals(forEntity1.getBody(), "Current profile is production");
	}

}
