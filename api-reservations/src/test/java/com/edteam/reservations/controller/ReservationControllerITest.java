package com.edteam.reservations.controller;

import com.edteam.reservations.util.TestUtil;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tags(@Tag("integration"))
@DisplayName("Check the functionality of the application")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerITest {

    public static MySQLContainer container = new MySQLContainer<>("mysql:8.2.0").withUsername("root")
            .withPassword("muppet").withDatabaseName("flights_reservation")
            .withCopyFileToContainer(MountableFile.forClasspathResource("/db/init.sql"),
                    "/docker-entrypoint-initdb.d/schema.sql")
            .withReuse(true);

    private static final String RESERVATION_URL = "/reservation";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationControllerITest.class);

    private MockMvc mockMvc;

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

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Order(1)
    @Tag("success-case")
    @DisplayName("should return the information of the reservation")
    @Test
    void get_should_return_a_reservation() throws Exception {

        LOGGER.info("Running get_should_return_a_reservation");

        // Given
        String responseBody = TestUtil.fromResponse("get-reservation-one-success-response.json");
        int request = 1;

        // When
        MvcResult mvcResult = mockMvc
                .perform(get(RESERVATION_URL.concat("/").concat(String.valueOf(request)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                // Then - Another option to check the results
                // .andExpect(status().isOk())
                // .andExpect(content().contentType(CONTENT_TYPE))
                // .andExpect(jsonPath("$.creationDate").value("2023-11-11"))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(responseBody, mvcResult.getResponse().getContentAsString()));
    }

    @Order(1)
    @Tag("success-case")
    @DisplayName("should return the information of the list of reservations")
    @Test
    void get_should_return_the_list_of_reservations() throws Exception {

        LOGGER.info("Running get_should_return_the_list_of_reservations");

        // Given
        String responseBody = TestUtil.fromResponse("get-reservation-list-success-response.json");
        String firstName = "Andres";

        // When
        MvcResult mvcResult = mockMvc.perform(get(RESERVATION_URL.concat("?firstName=").concat(firstName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(responseBody, mvcResult.getResponse().getContentAsString()));
    }

    @Order(2)
    @Tag("success-case")
    @DisplayName("should return a persisted reservation")
    @Test
    void insert_should_return_a_persisted_reservation() throws Exception {

        LOGGER.info("Running insert_should_return_a_persisted_reservation");

        // Given
        String requestBody = TestUtil.fromRequest("post-reservation-success-request.json");

        // When
        MvcResult mvcResult = mockMvc
                .perform(post(RESERVATION_URL).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertTrue(
                        mvcResult.getResponse().getContentAsString().contains("\"departure\":\"2023-12-31\"")));
    }

    @Order(3)
    @Tag("success-case")
    @DisplayName("should update a persisted reservation")
    @Test
    void update_should_change_a_persisted_reservation() throws Exception {

        LOGGER.info("Running update_should_change_a_persisted_reservation");

        // Given
        String requestBody = TestUtil.fromRequest("put-reservation-success-request.json");
        int requestId = 4;

        // When
        MvcResult mvcResult = mockMvc.perform(put(RESERVATION_URL.concat("/").concat(String.valueOf(requestId)))
                .content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertTrue(mvcResult.getResponse().getContentAsString().contains("\"firstName\":\"Jose\"")));
    }

    @Order(4)
    @Tag("success-case")
    @DisplayName("should remove a persisted reservation")
    @Test
    void delete_should_remove_a_persisted_reservation() throws Exception {

        LOGGER.info("Running delete_should_remove_a_persisted_reservation");

        // Given
        int request = 5;

        // When
        MvcResult mvcResult = mockMvc.perform(delete(RESERVATION_URL.concat("/").concat(String.valueOf(request)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus()));
    }

    @Tag("error-case")
    @DisplayName("should not return the information of the reservation")
    @Test
    void get_should_not_return_a_reservation() throws Exception {

        LOGGER.info("Running get_should_not_return_a_reservation");

        // Given
        String responseBody = TestUtil.fromResponse("get-reservation-one-error-response.json");
        int request = 1000;

        // When
        MvcResult mvcResult = mockMvc
                .perform(get(RESERVATION_URL.concat("/").concat(String.valueOf(request)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                // Then - Another option to check the results
                // .andExpect(status().isNotFound())
                // .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(responseBody, mvcResult.getResponse().getContentAsString()));
    }

    @Tag("error-case")
    @DisplayName("should not return the information of the list of reservations")
    @Test
    void get_should_not_return_the_list_of_reservations() throws Exception {

        LOGGER.info("Running get_should_not_return_the_list_of_reservations");

        // Given
        String responseBody = TestUtil.fromResponse("get-reservation-list-error-response.json");
        String firstName = "Andressssss";

        // When
        MvcResult mvcResult = mockMvc.perform(get(RESERVATION_URL.concat("?firstName=").concat(firstName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(responseBody, mvcResult.getResponse().getContentAsString()));
    }

    @Tag("error-case")
    @DisplayName("should not persist a reservation")
    @Test
    void insert_should_not_persist_a_reservation() throws Exception {

        LOGGER.info("Running insert_should_not_persist_a_reservation");

        // Given
        String requestBody = TestUtil.fromRequest("post-reservation-error-request.json");
        String responseBody = TestUtil.fromResponse("post-reservation-error-response.json");

        // When
        MvcResult mvcResult = mockMvc
                .perform(post(RESERVATION_URL).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(responseBody, mvcResult.getResponse().getContentAsString()));
    }

    @Tag("error-case")
    @DisplayName("should not update a reservation")
    @Test
    void update_should_not_change_a_reservation() throws Exception {

        LOGGER.info("Running update_should_not_change_a_reservation");

        // Given
        String requestBody = TestUtil.fromRequest("put-reservation-error-request.json");
        String responseBody = TestUtil.fromResponse("get-reservation-one-error-response.json");
        int requestId = 10000;

        // When
        MvcResult mvcResult = mockMvc.perform(put(RESERVATION_URL.concat("/").concat(String.valueOf(requestId)))
                .content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(responseBody, mvcResult.getResponse().getContentAsString()));
    }

    @Tag("error-case")
    @DisplayName("should not remove a reservation")
    @Test
    void delete_should_not_remove_a_reservation() throws Exception {

        LOGGER.info("Running delete_should_not_remove_a_reservation");

        // Given
        String responseBody = TestUtil.fromResponse("get-reservation-one-error-response.json");
        int request = 1000;

        // When
        MvcResult mvcResult = mockMvc.perform(delete(RESERVATION_URL.concat("/").concat(String.valueOf(request)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        // Then
        assertAll(() -> assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(responseBody, mvcResult.getResponse().getContentAsString()));
    }
}
