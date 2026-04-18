package com.myroom.reservation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myroom.reservation.PreferredLanguage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {

    @NotBlank(message = "{reservation.customerName.required}")
    @Size(max = 100)
    private String customerName;

    @NotBlank(message = "{reservation.phone.required}")
    @Size(max = 30)
    private String phone;

    @Email(message = "{reservation.email.invalid}")
    @Size(max = 150)
    private String email;

    @NotNull(message = "{reservation.partySize.required}")
    @Min(value = 1, message = "{reservation.partySize.min}")
    private Integer partySize;

    @NotNull(message = "{reservation.reservedAt.required}")
    @Future(message = "{reservation.reservedAt.future}")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservedAt;

    @Size(max = 1000)
    private String notes;

    private PreferredLanguage preferredLanguage;

    private Long roomId;
}
