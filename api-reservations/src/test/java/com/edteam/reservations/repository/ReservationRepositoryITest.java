package com.edteam.reservations.repository;

import com.edteam.reservations.model.Reservation;
import org.junit.jupiter.api.*;
import org.quickperf.junit5.QuickPerfTest;
import org.quickperf.spring.sql.QuickPerfSqlConfig;
import org.quickperf.sql.annotation.AnalyzeSql;
import org.quickperf.sql.annotation.ExpectInsert;
import org.quickperf.sql.annotation.ExpectMaxQueryExecutionTime;
import org.quickperf.sql.annotation.ExpectSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.util.List;

import static com.edteam.reservations.util.ReservationUtil.getReservation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tags(@Tag("integration"))
@DisplayName("Check the functionality of the repository")
@Import(QuickPerfSqlConfig.class)
@QuickPerfTest
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationRepositoryITest {

    @Autowired
    ReservationRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationRepositoryITest.class);

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
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @ExpectSelect(2) // Validate the number of queries that are executed
    @ExpectMaxQueryExecutionTime(thresholdInMilliSeconds = 8) // This check the duration of the execution of the query
    @AnalyzeSql
    @Tag("success-case")
    @DisplayName("should return the information of all the reservations")
    @Test
    void getReservations_should_return_the_information() {
        LOGGER.info("Running getReservations_should_return_the_information");

        // When
        List<Reservation> result = repository.findAll();

        // Then
        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result, hasSize(5)),
                () -> assertEquals(getReservation(1L, "BUE", "MIA").toString(), result.get(0).toString()),
                () -> assertThat(result.get(0), hasProperty("id")),
                () -> assertThat(result.get(0).getPassengers().get(0).getFirstName(), stringContainsInOrder("A", "s")),
                () -> assertThat(result.get(0).getPassengers().get(0).getFirstName(), matchesRegex("[a-zA-Z]+")));
    }

    @ExpectInsert(5) // Validate the number of queries that are executed
    @ExpectMaxQueryExecutionTime(thresholdInMilliSeconds = 8) // This check the duration of the execution of the query
    @AnalyzeSql
    @Tag("success-case")
    @DisplayName("should return the information of all the reservations")
    @Test
    void save_should_return_the_information() {

        LOGGER.info("Running save_should_return_the_information");

        // Given
        String origin = "BUE";
        String destination = "MIA";

        // When
        Reservation reservation = repository.save(getReservation(null, origin, destination));

        // Then
        assertAll(() -> assertNotNull(reservation),
                () -> assertEquals(origin, reservation.getItinerary().getSegment().get(0).getOrigin()),
                () -> assertEquals(destination, reservation.getItinerary().getSegment().get(0).getDestination()));
    }
}
