package com.edteam.reservations.service;

import com.edteam.reservations.util.BaseTest;
import com.edteam.reservations.connector.CatalogConnector;
import com.edteam.reservations.enums.APIError;
import com.edteam.reservations.exception.EdteamException;
import com.edteam.reservations.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.*;

@Tags(@Tag("integration"))
@DisplayName("Check the functionality of the service")
class ReservationServiceITest extends BaseTest {

    @Autowired
    ReservationRepository repository;

    @Autowired
    ConversionService conversionService;

    @Autowired
    CatalogConnector catalogConnector;

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
