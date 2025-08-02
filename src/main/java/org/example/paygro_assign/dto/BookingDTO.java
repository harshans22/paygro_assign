package org.example.paygro_assign.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.paygro_assign.enums.BookingStatus;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private UUID id;

    @NotNull
    private UUID loadId;

    @NotBlank
    private String transporterId;

    @Positive
    private double proposedRate;

    private String comment;

    private BookingStatus status;

    private Instant requestedAt;
}
