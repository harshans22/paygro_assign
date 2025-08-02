package org.example.paygro_assign.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO {
    @NotBlank
    private String loadingPoint;
    @NotBlank
    private String unloadingPoint;
    @NotNull
    private Instant loadingDate;
    @NotNull
    private Instant unloadingDate;
}
