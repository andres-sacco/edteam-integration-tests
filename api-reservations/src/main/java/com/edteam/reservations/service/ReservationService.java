package com.edteam.reservations.service;

import com.edteam.reservations.connector.CatalogConnector;
import com.edteam.reservations.connector.response.CityDTO;
import com.edteam.reservations.dto.SearchReservationCriteriaDTO;
import com.edteam.reservations.dto.SegmentDTO;
import com.edteam.reservations.enums.APIError;
import com.edteam.reservations.exception.EdteamException;
import com.edteam.reservations.dto.ReservationDTO;
import com.edteam.reservations.model.Reservation;
import com.edteam.reservations.repository.ReservationRepository;
import com.edteam.reservations.specification.ReservationSpecification;
import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    private ReservationRepository repository;

    private ConversionService conversionService;

    private CatalogConnector catalogConnector;

    @Autowired
    public ReservationService(ReservationRepository repository, ConversionService conversionService,
            CatalogConnector catalogConnector) {
        this.repository = repository;
        this.conversionService = conversionService;
        this.catalogConnector = catalogConnector;
    }

    public List<ReservationDTO> getReservations(SearchReservationCriteriaDTO criteria) {
        Pageable pageable = PageRequest.of(criteria.getPageActual(), criteria.getPageSize());
        return conversionService.convert(
                repository.findAll(ReservationSpecification.withSearchCriteria(criteria), pageable), List.class);
    }

    public ReservationDTO getReservationById(Long id) {
        Optional<Reservation> result = repository.findById(id);
        if (result.isEmpty()) {
            LOGGER.debug("Not exist reservation with the id {}", id);
            throw new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }
        return conversionService.convert(result.get(), ReservationDTO.class);
    }

    public ReservationDTO save(ReservationDTO reservation) {
        if (Objects.nonNull(reservation.getId())) {
            throw new EdteamException(APIError.RESERVATION_WITH_SAME_ID);
        }
        checkCity(reservation);

        Reservation transformed = conversionService.convert(reservation, Reservation.class);
        validateEntity(transformed);

        Reservation result = repository.save(Objects.requireNonNull(transformed));
        return conversionService.convert(result, ReservationDTO.class);
    }

    public ReservationDTO update(Long id, ReservationDTO reservation) {
        if (!repository.existsById(id)) {
            LOGGER.debug("Not exist reservation with the id {}", id);
            throw new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }
        checkCity(reservation);

        Reservation transformed = conversionService.convert(reservation, Reservation.class);
        validateEntity(transformed);
        Reservation result = repository.save(Objects.requireNonNull(transformed));
        return conversionService.convert(result, ReservationDTO.class);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            LOGGER.debug("Not exist reservation with the id {}", id);
            throw new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }

        repository.deleteById(id);
    }

    private void checkCity(ReservationDTO reservationDTO) {
        try {
            for (SegmentDTO segmentDTO : reservationDTO.getItinerary().getSegment()) {
                CityDTO origin = catalogConnector.getCity(segmentDTO.getOrigin());
                CityDTO destination = catalogConnector.getCity(segmentDTO.getDestination());

                if (origin == null || destination == null) {
                    throw new EdteamException(APIError.VALIDATION_ERROR);
                }
            }
        } catch (Exception e) {
            throw new EdteamException(APIError.VALIDATION_ERROR);
        }
    }

    private void validateEntity(Reservation transformed) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Reservation>> violations = validator.validate(transformed);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
