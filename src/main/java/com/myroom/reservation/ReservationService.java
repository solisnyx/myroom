package com.myroom.reservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myroom.auth.AuthPrincipal;
import com.myroom.common.BadRequestException;
import com.myroom.common.ForbiddenException;
import com.myroom.common.NotFoundException;
import com.myroom.reservation.dto.ReservationRequest;
import com.myroom.room.Room;
import com.myroom.room.RoomRepository;
import com.myroom.user.User;
import com.myroom.user.UserRepository;
import com.myroom.user.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public List<Reservation> findAll(ReservationStatus status, LocalDateTime from, LocalDateTime to) {
        if (status != null) {
            return reservationRepository.findByStatus(status);
        }
        if (from != null && to != null) {
            return reservationRepository.findByReservedAtBetween(from, to);
        }
        return reservationRepository.findAll();
    }

    public List<Reservation> findMine(Long userId) {
        return reservationRepository.findByUserIdOrderByReservedAtDesc(userId);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("reservation.notFound", id));
    }

    public Reservation findAccessible(Long id, AuthPrincipal principal) {
        Reservation r = findById(id);
        authorize(r, principal);
        return r;
    }

    @Transactional
    public Reservation create(ReservationRequest request, AuthPrincipal principal) {
        Room room = resolveRoom(request.getRoomId(), request.getPartySize());
        User user = principal == null ? null : userRepository.findById(principal.id())
                .orElseThrow(() -> new NotFoundException("user.notFound", principal.id()));

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
                .user(user)
                .build();
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation update(Long id, ReservationRequest request, AuthPrincipal principal) {
        Reservation r = findById(id);
        authorize(r, principal);
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
    public Reservation changeStatus(Long id, ReservationStatus newStatus, AuthPrincipal principal) {
        Reservation r = findById(id);
        if (newStatus == ReservationStatus.CONFIRMED && !isAdmin(principal)) {
            throw new ForbiddenException("error.forbidden");
        }
        authorize(r, principal);
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

    private void authorize(Reservation r, AuthPrincipal principal) {
        if (isAdmin(principal)) {
            return;
        }
        if (principal == null || r.getUser() == null
                || !principal.id().equals(r.getUser().getId())) {
            throw new ForbiddenException("error.forbidden");
        }
    }

    private boolean isAdmin(AuthPrincipal principal) {
        return principal != null && principal.role() == UserRole.ADMIN;
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
