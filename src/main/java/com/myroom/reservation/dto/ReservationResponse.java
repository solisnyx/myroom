package com.myroom.reservation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myroom.reservation.PreferredLanguage;
import com.myroom.reservation.Reservation;
import com.myroom.reservation.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private String customerName;
    private String phone;
    private String email;
    private Integer partySize;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservedAt;

    private String notes;
    private ReservationStatus status;
    private PreferredLanguage preferredLanguage;
    private Long roomId;
    private String roomName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public static ReservationResponse from(Reservation r) {
        return ReservationResponse.builder()
                .id(r.getId())
                .customerName(r.getCustomerName())
                .phone(r.getPhone())
                .email(r.getEmail())
                .partySize(r.getPartySize())
                .reservedAt(r.getReservedAt())
                .notes(r.getNotes())
                .status(r.getStatus())
                .preferredLanguage(r.getPreferredLanguage())
                .roomId(r.getRoom() == null ? null : r.getRoom().getId())
                .roomName(r.getRoom() == null ? null : r.getRoom().getName())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
