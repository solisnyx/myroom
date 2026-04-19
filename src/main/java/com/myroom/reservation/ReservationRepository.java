package com.myroom.reservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByReservedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByUserIdOrderByReservedAtDesc(Long userId);
}
