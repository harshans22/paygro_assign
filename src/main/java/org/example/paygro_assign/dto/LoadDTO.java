package org.example.paygro_assign.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.paygro_assign.enums.LoadStatus;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoadDTO {
    private UUID id;

    @NotBlank
    private String shipperId;

    @Valid
    private FacilityDTO facility;

    @NotBlank
    private String productType;

    @NotBlank
    private String truckType;

    @Min(1)
    private int noOfTrucks;

    @Positive
    private double weight;

    private String comment;
    private Instant datePosted;
    private LoadStatus status;
}
