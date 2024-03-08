package com.edteam.reservations.service;

import com.edteam.reservations.connector.CatalogConnector;
import com.edteam.reservations.enums.APIError;
import com.edteam.reservations.exception.EdteamException;
import com.edteam.reservations.repository.ReservationRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static org.junit.jupiter.api.Assertions.*;

@Tags(@Tag("integration"))
@DisplayName("Check the functionality of the service")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationServiceITest {

    public static MySQLContainer container = new MySQLContainer<>("mysql:8.2.0").withUsername("root")
            .withPassword("muppet").withDatabaseName("flights_reservation")
            .withCopyFileToContainer(MountableFile.forClasspathResource("/db/init.sql"),
                    "/docker-entrypoint-initdb.d/schema.sql")
            .withReuse(true);

    private static WireMockServer wireMockServer;

    @Autowired
    ReservationRepository repository;

    @Autowired
    ConversionService conversionService;

    @Autowired
    CatalogConnector catalogConnector;

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
    static void sqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Tag("error-case")
    @DisplayName("should not return the information of the reservation")
    @Test
    void getReservation_should_not_return_the_information() {

        // Given
        ReservationService service = new ReservationService(repository, conversionService, catalogConnector);

        // When
        EdteamException exception = assertThrows(EdteamException.class, () -> {
            service.getReservationById(1110L);
        });

        // Then
        assertAll(() -> assertNotNull(exception),
                () -> assertEquals(APIError.RESERVATION_NOT_FOUND.getMessage(), exception.getDescription()),
                () -> assertEquals(APIError.RESERVATION_NOT_FOUND.getHttpStatus(), exception.getStatus()));
    }

    @Tag("error-case")
    @DisplayName("should not remove a reservation")
    @Test
    void delete_should_not_remove_a_reservation() throws Exception {

        // Given
        ReservationService service = new ReservationService(repository, conversionService, catalogConnector);

        // When
        EdteamException exception = assertThrows(EdteamException.class, () -> {
            service.delete(1110L);
        });

        // Then
        assertAll(() -> assertNotNull(exception),
                () -> assertEquals(APIError.RESERVATION_NOT_FOUND.getMessage(), exception.getDescription()),
                () -> assertEquals(APIError.RESERVATION_NOT_FOUND.getHttpStatus(), exception.getStatus()));
    }
}
