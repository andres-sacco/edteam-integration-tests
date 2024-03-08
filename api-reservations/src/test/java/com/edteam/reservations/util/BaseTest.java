package com.edteam.reservations.util;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseTest {

    private static WireMockServer wireMockServer;
    public static MySQLContainer container = new MySQLContainer<>("mysql:8.2.0").withUsername("root")
            .withPassword("muppet").withDatabaseName("flights_reservation")
            .withCopyFileToContainer(MountableFile.forClasspathResource("/db/init.sql"),
                    "/docker-entrypoint-initdb.d/schema.sql")
            .withReuse(true);

    @BeforeAll
    public static void setUp() {
        container.start();

        // Configure WireMock to use the mappings directory
        // This assumes your mappings are in the classpath under "wiremock" directory
        WireMockConfiguration config = WireMockConfiguration.options()
                .usingFilesUnderClasspath("src/test/resources/wiremock");
        config.port(6070);

        // Create a new WireMockServer instance
        wireMockServer = new WireMockServer(config);

        // Start the server
        wireMockServer.start();
    }

    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void qlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
