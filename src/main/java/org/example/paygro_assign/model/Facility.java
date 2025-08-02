package org.example.paygro_assign.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

// com.cargopro.model.Facility.java
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facility {
    @Column(nullable = false)
    private String loadingPoint;
    @Column(nullable = false)
    private String unloadingPoint;
    @Column(nullable = false)
    private Instant loadingDate;
    @Column(nullable = false)
    private Instant unloadingDate;
}
