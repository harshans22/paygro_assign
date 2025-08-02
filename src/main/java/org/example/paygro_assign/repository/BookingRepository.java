package org.example.paygro_assign.repository;

import org.example.paygro_assign.enums.BookingStatus;
import org.example.paygro_assign.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByLoadId(UUID loadId);

    List<Booking> findByLoadIdAndStatus(UUID loadId, BookingStatus status);

    List<Booking> findByTransporterId(String transporterId);

    List<Booking> findByLoadIdAndTransporterIdAndStatus(UUID loadId, String transporterId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE "
        + "(:loadId IS NULL OR b.load.id = :loadId) "
        + "AND (:transporterId IS NULL OR b.transporterId = :transporterId) "
        + "AND (:status IS NULL OR b.status = :status)")
    List<Booking> filterBookings(
        @Param("loadId") UUID loadId,
        @Param("transporterId") String transporterId,
        @Param("status") BookingStatus status
    );

    long countByLoadIdAndStatus(UUID loadId, BookingStatus status);
    long countByLoadId(UUID loadId);
}
