package com.edteam.reservations.controller;

import com.edteam.reservations.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Check the documentation of the application")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DocumentationITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationITest.class);

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Tag("success-case")
    @DisplayName("should return redirect to another url")
    @Test
    void get_documentation_should_redirect() throws Exception {

        LOGGER.info("Running get_documentation_should_redirect");

        // When
        mockMvc.perform(get("/documentation"))
                // Then
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("swagger-ui.html"));
    }

    @Tag("success-case")
    @DisplayName("should return the information of the documentation")
    @Test
    void get_should_return_the_documentation() throws Exception {

        LOGGER.info("Running get_documentation_should_return_a_html");

        // Given
        String response = TestUtil.fromResponse("get-documentation-success-response.html");

        // When
        MvcResult mvcResult = mockMvc.perform(get("/swagger-ui/index.html")).andReturn();

        // Then
        assertAll(() -> assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus()),
                () -> assertEquals(MediaType.TEXT_HTML_VALUE, mvcResult.getResponse().getContentType()),
                () -> assertEquals(response, mvcResult.getResponse().getContentAsString()));
    }
}
