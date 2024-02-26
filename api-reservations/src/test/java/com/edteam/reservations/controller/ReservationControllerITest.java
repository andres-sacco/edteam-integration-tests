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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Tag("success-case")
    @DisplayName("should return the information of the reservation")
    @Test
    void get_should_return_a_reservation() throws Exception {

        LOGGER.info("Running get_should_return_a_reservation");

        // Given
        int request = 1;

        // When
        MvcResult mvcResult = mockMvc.perform(get("/reservation/".concat(String.valueOf(request))).contentType(CONTENT_TYPE))
                // Then - Another option to check the results
                //.andExpect(status().isOk())
                //.andExpect(content().contentType(CONTENT_TYPE))
                //.andExpect(jsonPath("$.creationDate").value("2023-11-11"))
                .andReturn();
        // Then
        assertAll(
                () -> assertEquals(200, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType()),
                () -> assertTrue(mvcResult.getResponse().getContentAsString().contains("2023-11-11"))
        );
    }


    @Tag("error-case")
    @DisplayName("should not return the information of the reservation")
    @Test
    void get_should_not_return_a_reservation() throws Exception {

        LOGGER.info("Running get_should_not_return_a_reservation");

        // Given
        int request = 1000;

        // When
        MvcResult mvcResult = mockMvc.perform(get("/reservation/".concat(String.valueOf(request))).contentType(CONTENT_TYPE))
                // Then - Another option to check the results
                //.andExpect(status().isNotFound())
                //.andExpect(content().contentType(CONTENT_TYPE))
                .andReturn();
        // Then
        assertAll(
                () -> assertEquals(404, mvcResult.getResponse().getStatus()),
                () -> assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType())
        );
    }
}
