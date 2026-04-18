package com.myroom.reservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myroom.common.BadRequestException;
import com.myroom.common.NotFoundException;
import com.myroom.reservation.dto.ReservationRequest;
import com.myroom.room.Room;
import com.myroom.room.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public List<Reservation> findAll(ReservationStatus status, LocalDateTime from, LocalDateTime to) {
        if (status != null) {
            return reservationRepository.findByStatus(status);
        }
        if (from != null && to != null) {
            return reservationRepository.findByReservedAtBetween(from, to);
        }
        return reservationRepository.findAll();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("reservation.notFound", id));
    }

    @Transactional
    public Reservation create(ReservationRequest request) {
        Room room = resolveRoom(request.getRoomId(), request.getPartySize());

        Reservation reservation = Reservation.builder()
                .customerName(request.getCustomerName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .partySize(request.getPartySize())
                .reservedAt(request.getReservedAt())
                .notes(request.getNotes())
                .status(ReservationStatus.PENDING)
                .preferredLanguage(resolveLanguage(request.getPreferredLanguage()))
                .room(room)
                .build();
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation update(Long id, ReservationRequest request) {
        Reservation r = findById(id);
        if (r.getStatus() == ReservationStatus.CANCELLED) {
            throw new BadRequestException("reservation.cancelled.notModifiable");
        }
        Room room = resolveRoom(request.getRoomId(), request.getPartySize());
        r.setCustomerName(request.getCustomerName());
        r.setPhone(request.getPhone());
        r.setEmail(request.getEmail());
        r.setPartySize(request.getPartySize());
        r.setReservedAt(request.getReservedAt());
        r.setNotes(request.getNotes());
        r.setPreferredLanguage(resolveLanguage(request.getPreferredLanguage()));
        r.setRoom(room);
        return r;
    }

    @Transactional
    public Reservation changeStatus(Long id, ReservationStatus newStatus) {
        Reservation r = findById(id);
        if (r.getStatus() == ReservationStatus.CANCELLED) {
            throw new BadRequestException("reservation.cancelled.notModifiable");
        }
        r.setStatus(newStatus);
        return r;
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NotFoundException("reservation.notFound", id);
        }
        reservationRepository.deleteById(id);
    }

    private Room resolveRoom(Long roomId, Integer partySize) {
        if (roomId == null) {
            return null;
        }
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("room.notFound", roomId));
        if (partySize != null && room.getCapacity() != null && partySize > room.getCapacity()) {
            throw new BadRequestException("reservation.partySize.exceedsCapacity",
                    partySize, room.getCapacity());
        }
        return room;
    }

    private PreferredLanguage resolveLanguage(PreferredLanguage requested) {
        if (requested != null) {
            return requested;
        }
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return "ko".equalsIgnoreCase(lang) ? PreferredLanguage.KO : PreferredLanguage.JA;
    }
}
