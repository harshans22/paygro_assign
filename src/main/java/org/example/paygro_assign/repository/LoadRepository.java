package org.example.paygro_assign.repository;

import org.example.paygro_assign.enums.LoadStatus;
import org.example.paygro_assign.model.Load;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;


public interface LoadRepository extends JpaRepository<Load, UUID>, JpaSpecificationExecutor<Load> {
    Page<Load> findAllByShipperIdAndTruckTypeAndStatus(String shipperId, String truckType, LoadStatus status, Pageable pageable);

    Page<Load> findAllByShipperIdAndTruckType(String shipperId, String truckType, Pageable pageable);

    Page<Load> findAllByShipperIdAndStatus(String shipperId, LoadStatus status, Pageable pageable);

    Page<Load> findAllByTruckTypeAndStatus(String truckType, LoadStatus status, Pageable pageable);

    // Use Specification for more complex filtering in practice
}
