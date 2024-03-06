package com.edteam.reservations.controller;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Check the webpage")
class ExampleControllerITest {

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }

    @AfterEach
    public void tearDown() {
        webClient.close();
    }

    @Tag("success-case")
    @DisplayName("should return the html")
    @Test
    void get_should_return_the_webpage() throws Exception {

        // Given
        String url = "https://example.org/";

        // When
        HtmlPage result = webClient.getPage(url);

        // Then
        assertAll(() -> assertEquals("Example Domain", result.getTitleText()),
                () -> assertEquals("text/html; charset=UTF-8",
                        result.getWebResponse().getResponseHeaderValue("Content-Type")),
                () -> assertThat(result.getElementsByTagName("head").getLength()).isPositive(),
                () -> assertThat(result.getElementsByTagName("h1").getLength()).isPositive(),
                () -> assertThat((HtmlAnchor) result.getFirstByXPath("//a")).isNotNull(),
                () -> assertThat(((HtmlAnchor) result.getFirstByXPath("//a")).getHrefAttribute())
                        .isEqualTo("https://www.iana.org/domains/example")

        );
    }
}
