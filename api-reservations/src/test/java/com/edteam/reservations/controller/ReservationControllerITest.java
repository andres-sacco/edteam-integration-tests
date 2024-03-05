package com.edteam.reservations.controller;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tags(@Tag("integration"))
@DisplayName("Check the functionality of the application")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerITest {

    public static final String CONTENT_TYPE = "application/json";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationControllerITest.class);

    private MockMvc mockMvc;

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
        int request = 1;

        // When
        MvcResult mvcResult = mockMvc
                .perform(get("/reservation/".concat(String.valueOf(request))).contentType(CONTENT_TYPE))
                // Then - Another option to check the results
                // .andExpect(status().isOk())
                // .andExpect(content().contentType(CONTENT_TYPE))
                // .andExpect(jsonPath("$.creationDate").value("2023-11-11"))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(200, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(
                        "{\"id\":1,\"version\":3,\"passengers\":[{\"firstName\":\"Andres\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554713\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"},{\"firstName\":\"Andres\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554718\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"}],\"itinerary\":{\"id\":1,\"version\":4,\"segment\":[{\"origin\":\"BUE\",\"destination\":\"MIA\",\"departure\":\"2023-12-31\",\"arrival\":\"2024-01-01\",\"carrier\":\"AA\"}],\"price\":{\"totalPrice\":30.00,\"totalTax\":20.00,\"basePrice\":10.00}},\"creationDate\":\"2023-11-11\"}",
                        mvcResult.getResponse().getContentAsString()));
    }

    @Order(1)
    @Tag("success-case")
    @DisplayName("should return the information of the list of reservations")
    @Test
    void get_should_return_the_list_of_reservations() throws Exception {

        LOGGER.info("Running get_should_return_the_list_of_reservations");

        // Given
        String firstName = "Andres";

        // When
        MvcResult mvcResult = mockMvc
                .perform(get("/reservation?firstName=".concat(firstName)).contentType(CONTENT_TYPE)).andReturn();
        // Then
        assertAll(() -> assertEquals(200, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(
                        "[{\"id\":1,\"version\":3,\"passengers\":[{\"firstName\":\"Andres\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554713\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"},{\"firstName\":\"Andres\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554718\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"}],\"itinerary\":{\"id\":1,\"version\":4,\"segment\":[{\"origin\":\"BUE\",\"destination\":\"MIA\",\"departure\":\"2023-12-31\",\"arrival\":\"2024-01-01\",\"carrier\":\"AA\"}],\"price\":{\"totalPrice\":30.00,\"totalTax\":20.00,\"basePrice\":10.00}},\"creationDate\":\"2023-11-11\"}]",
                        mvcResult.getResponse().getContentAsString()));
    }

    @Order(2)
    @Tag("success-case")
    @DisplayName("should return a persisted reservation")
    @Test
    void insert_should_return_a_persisted_reservation() throws Exception {

        LOGGER.info("Running insert_should_return_a_persisted_reservation");

        // Given
        String request = "{\"passengers\":[{\"firstName\":\"Jose\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554713\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"}],\"itinerary\":{\"segment\":[{\"origin\":\"BUE\",\"destination\":\"MIA\",\"departure\":\"2023-12-31\",\"arrival\":\"2024-01-01\",\"carrier\":\"AA\"}],\"price\":{\"totalPrice\":30.00,\"totalTax\":20.00,\"basePrice\":10.00}},\"creationDate\":\"2023-11-11\"}";

        // When
        MvcResult mvcResult = mockMvc.perform(post("/reservation").content(request).contentType(CONTENT_TYPE))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(201, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()), () -> assertTrue(
                        mvcResult.getResponse().getContentAsString().contains("\"departure\":\"2023-12-31\"")));
    }

    @Order(3)
    @Tag("success-case")
    @DisplayName("should update a persisted reservation")
    @Test
    void update_should_change_a_persisted_reservation() throws Exception {

        LOGGER.info("Running update_should_change_a_persisted_reservation");

        // Given
        String requestBody = "{\"id\":4,\"version\":2,\"passengers\":[{\"firstName\":\"Jose\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554713\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"},{\"firstName\":\"Andres\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554718\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"}],\"itinerary\":{\"id\":4,\"version\":2,\"segment\":[{\"origin\":\"BUE\",\"destination\":\"MIA\",\"departure\":\"2023-12-31\",\"arrival\":\"2024-01-01\",\"carrier\":\"AA\"}],\"price\":{\"totalPrice\":30.00,\"totalTax\":20.00,\"basePrice\":10.00}},\"creationDate\":\"2023-11-11\"}";
        int requestId = 4;

        // When
        MvcResult mvcResult = mockMvc.perform(
                put("/reservation/".concat(String.valueOf(requestId))).content(requestBody).contentType(CONTENT_TYPE))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(200, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()),
                () -> assertTrue(mvcResult.getResponse().getContentAsString().contains("\"firstName\":\"Jose\"")));
    }

    @Order(4)
    @Tag("success-case")
    @DisplayName("should remove a persisted reservation")
    @Test
    void delete_should_remove_a_persisted_reservation() throws Exception {

        LOGGER.info("Running delete_should_remove_a_persisted_reservation");

        // Given
        int request = 10;

        // When
        MvcResult mvcResult = mockMvc
                .perform(delete("/reservation/".concat(String.valueOf(request))).contentType(CONTENT_TYPE)).andReturn();
        // Then
        assertAll(() -> assertEquals(200, mvcResult.getResponse().getStatus()));
    }

    @Tag("error-case")
    @DisplayName("should not return the information of the reservation")
    @Test
    void get_should_not_return_a_reservation() throws Exception {

        LOGGER.info("Running get_should_not_return_a_reservation");

        // Given
        int request = 1000;

        // When
        MvcResult mvcResult = mockMvc
                .perform(get("/reservation/".concat(String.valueOf(request))).contentType(CONTENT_TYPE))
                // Then - Another option to check the results
                // .andExpect(status().isNotFound())
                // .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(404, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()),
                () -> assertEquals("{\"description\":\"Reservation not found\",\"reasons\":null}",
                        mvcResult.getResponse().getContentAsString()));
    }

    @Tag("error-case")
    @DisplayName("should not return the information of the list of reservations")
    @Test
    void get_should_not_return_the_list_of_reservations() throws Exception {

        LOGGER.info("Running get_should_not_return_the_list_of_reservations");

        // Given
        String firstName = "Andressssss";

        // When
        MvcResult mvcResult = mockMvc
                .perform(get("/reservation?firstName=".concat(firstName)).contentType(CONTENT_TYPE)).andReturn();
        // Then
        assertAll(() -> assertEquals(200, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()),
                () -> assertEquals("[]", mvcResult.getResponse().getContentAsString()));
    }

    @Tag("error-case")
    @DisplayName("should not persist a reservation")
    @Test
    void insert_should_not_persist_a_reservation() throws Exception {

        LOGGER.info("Running insert_should_not_persist_a_reservation");

        // Given
        String request = "{\"passengers\":[{\"firstName\":\"Jose\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554713\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"}],\"itinerary\":{\"segment\":[{\"origin\":\"LAS\",\"destination\":\"MIA\",\"departure\":\"2023-12-31\",\"arrival\":\"2024-01-01\",\"carrier\":\"AA\"}],\"price\":{\"totalPrice\":30.00,\"totalTax\":20.00,\"basePrice\":10.00}},\"creationDate\":\"2023-11-11\"}";

        // When
        MvcResult mvcResult = mockMvc.perform(post("/reservation").content(request).contentType(CONTENT_TYPE))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(400, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()),
                () -> assertEquals("{\"description\":\"The are attributes with wrong values\",\"reasons\":null}",
                        mvcResult.getResponse().getContentAsString()));
    }

    @Tag("error-case")
    @DisplayName("should not update a reservation")
    @Test
    void update_should_not_change_a_reservation() throws Exception {

        LOGGER.info("Running update_should_not_change_a_reservation");

        // Given
        String requestBody = "{\"id\":10000,\"version\":2,\"passengers\":[{\"firstName\":\"Jose\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554713\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"},{\"firstName\":\"Andres\",\"lastName\":\"Sacco\",\"documentNumber\":\"AB554718\",\"documentType\":\"PASSPORT\",\"birthday\":\"1985-01-01\"}],\"itinerary\":{\"id\":4,\"version\":2,\"segment\":[{\"origin\":\"BUE\",\"destination\":\"MIA\",\"departure\":\"2023-12-31\",\"arrival\":\"2024-01-01\",\"carrier\":\"AA\"}],\"price\":{\"totalPrice\":30.00,\"totalTax\":20.00,\"basePrice\":10.00}},\"creationDate\":\"2023-11-11\"}";
        int requestId = 10000;

        // When
        MvcResult mvcResult = mockMvc.perform(
                put("/reservation/".concat(String.valueOf(requestId))).content(requestBody).contentType(CONTENT_TYPE))
                .andReturn();
        // Then
        assertAll(() -> assertEquals(404, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()),
                () -> assertEquals("{\"description\":\"Reservation not found\",\"reasons\":null}",
                        mvcResult.getResponse().getContentAsString()));
    }

    @Tag("error-case")
    @DisplayName("should not remove a reservation")
    @Test
    void delete_should_not_remove_a_reservation() throws Exception {

        LOGGER.info("Running delete_should_not_remove_a_reservation");

        // Given
        int request = 1000;

        // When
        MvcResult mvcResult = mockMvc
                .perform(delete("/reservation/".concat(String.valueOf(request))).contentType(CONTENT_TYPE)).andReturn();
        // Then
        assertAll(() -> assertEquals(404, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()),
                () -> assertEquals("{\"description\":\"Reservation not found\",\"reasons\":null}",
                        mvcResult.getResponse().getContentAsString()));
    }
}
