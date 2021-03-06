package com.test.taskmanagement;

import com.test.taskmanagement.init.PostgresInitializer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(
		initializers = {PostgresInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FlywayTest(locationsForMigrate = {
		"classpath:db/migration",
		"test/resources/db/migration"
})
@AutoConfigureWebTestClient(timeout = "10000")
@FieldDefaults(level = AccessLevel.PROTECTED)
public class TaskManagementApplicationTests {

	@Autowired
	WebTestClient webTestClient;

}
