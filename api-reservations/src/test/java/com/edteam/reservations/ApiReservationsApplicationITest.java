package com.edteam.reservations;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
@SpringBootTest
class ApiReservationsApplicationITest {

    public static MySQLContainer container = new MySQLContainer<>("mysql:8.2.0").withUsername("root")
            .withPassword("muppet").withDatabaseName("flights_reservation")
            .withCopyFileToContainer(MountableFile.forClasspathResource("/db/init.sql"),
                    "/docker-entrypoint-initdb.d/schema.sql")
            .withReuse(true);

    @BeforeAll
    public static void setUp() {
        container.start();
    }

    @DynamicPropertySource
    static void sqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    void contextLoad() {

    }
}
