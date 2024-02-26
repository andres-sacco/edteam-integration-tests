package com.edteam.reservations.repository;

import com.edteam.reservations.model.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Transactional(readOnly = true, timeout = 30)
    List<Reservation> findAll(Specification<Reservation> specification, Pageable pageable);

}