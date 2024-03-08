package com.edteam.reservations.util;

import com.edteam.reservations.dto.*;
import com.edteam.reservations.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReservationUtil {

    public static ReservationDTO getReservationDTO(Long id, String origin, String destination) {
        PassengerDTO passenger = new PassengerDTO();
        passenger.setFirstName("Andres");
        passenger.setLastName("Sacco");
        passenger.setDocumentType("DNI");
        passenger.setDocumentNumber("12345678");
        passenger.setBirthday(LocalDate.of(1985, 1, 1));

        PriceDTO price = new PriceDTO();
        price.setBasePrice(BigDecimal.ONE);
        price.setTotalTax(BigDecimal.ZERO);
        price.setTotalPrice(BigDecimal.ONE);

        SegmentDTO segment = new SegmentDTO();
        segment.setArrival("2025-01-01");
        segment.setDeparture("2024-12-31");
        segment.setOrigin(origin);
        segment.setDestination(destination);
        segment.setCarrier("AA");

        ItineraryDTO itinerary = new ItineraryDTO();
        itinerary.setPrice(price);
        itinerary.setSegment(List.of(segment));

        ReservationDTO reservation = new ReservationDTO();
        reservation.setId(id);
        reservation.setPassengers(List.of(passenger));
        reservation.setItinerary(itinerary);

        return reservation;
    }

    public static Reservation getReservation(Long id, String origin, String destination) {
        Passenger passenger = new Passenger();
        passenger.setFirstName("Andres");
        passenger.setLastName("Sacco");
        passenger.setId(id);

        passenger.setDocumentType("DNI");
        passenger.setDocumentNumber("12345678");
        passenger.setBirthday(LocalDate.of(1985, 1, 1));

        Price price = new Price();
        price.setBasePrice(BigDecimal.ONE);
        price.setTotalTax(BigDecimal.ZERO);
        price.setTotalPrice(BigDecimal.ONE);

        Segment segment = new Segment();
        segment.setArrival("2025-01-01");
        segment.setDeparture("2024-12-31");
        segment.setOrigin(origin);
        segment.setDestination(destination);
        segment.setCarrier("AA");
        segment.setId(id);

        Itinerary itinerary = new Itinerary();
        itinerary.setId(id);
        itinerary.setPrice(price);
        itinerary.setSegment(List.of(segment));

        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setPassengers(List.of(passenger));
        reservation.setItinerary(itinerary);
        reservation.setCreationDate(LocalDate.of(2023, 11, 11));

        return reservation;
    }
}