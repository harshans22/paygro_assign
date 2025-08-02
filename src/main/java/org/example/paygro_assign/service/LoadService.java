package org.example.paygro_assign.service;

import lombok.RequiredArgsConstructor;
import org.example.paygro_assign.dto.FacilityDTO;
import org.example.paygro_assign.dto.LoadDTO;
import org.example.paygro_assign.enums.LoadStatus;
import org.example.paygro_assign.exceptions.BadRequestException;
import org.example.paygro_assign.exceptions.NotFoundException;
import org.example.paygro_assign.model.Facility;
import org.example.paygro_assign.model.Load;
import org.example.paygro_assign.repository.BookingRepository;
import org.example.paygro_assign.repository.LoadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

// com.cargopro.service.LoadService.java
@Service
@RequiredArgsConstructor
public class LoadService {
    private final LoadRepository loadRepo;
    private final BookingRepository bookingRepo;

    public LoadDTO createLoad(LoadDTO dto) {
        Load load = Load.builder()
            .shipperId(dto.getShipperId())
            .facility(new Facility(
                dto.getFacility().getLoadingPoint(),
                dto.getFacility().getUnloadingPoint(),
                dto.getFacility().getLoadingDate(),
                dto.getFacility().getUnloadingDate()
            ))
            .productType(dto.getProductType())
            .truckType(dto.getTruckType())
            .noOfTrucks(dto.getNoOfTrucks())
            .weight(dto.getWeight())
            .comment(dto.getComment())
            .datePosted(Instant.now())
            .status(LoadStatus.POSTED)
            .build();
        load = loadRepo.save(load);
        return mapToDTO(load);
    }

    public LoadDTO getLoad(UUID loadId) {
        Load load = loadRepo.findById(loadId).orElseThrow(() -> new NotFoundException("Load not found"));
        return mapToDTO(load);
    }

    public Page<LoadDTO> listLoads(String shipperId, String truckType, LoadStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("datePosted").descending());
        Specification<Load> spec = (root, query, cb) -> cb.conjunction();
        if (shipperId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("shipperId"), shipperId));
        if (truckType != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("truckType"), truckType));
        if (status != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
        Page<Load> loads = loadRepo.findAll(spec, pageable);
        return loads.map(this::mapToDTO);
    }

    public LoadDTO updateLoad(UUID loadId, LoadDTO dto) {
        Load load = loadRepo.findById(loadId).orElseThrow(() -> new NotFoundException("Load not found"));
        if (load.getStatus() == LoadStatus.CANCELLED)
            throw new BadRequestException("Cannot update a cancelled load");
        load.setProductType(dto.getProductType());
        load.setTruckType(dto.getTruckType());
        load.setNoOfTrucks(dto.getNoOfTrucks());
        load.setWeight(dto.getWeight());
        load.setComment(dto.getComment());
        load.setFacility(new Facility(
            dto.getFacility().getLoadingPoint(),
            dto.getFacility().getUnloadingPoint(),
            dto.getFacility().getLoadingDate(),
            dto.getFacility().getUnloadingDate()
        ));
        loadRepo.save(load);
        return mapToDTO(load);
    }

    public void deleteLoad(UUID loadId) {
        if (!loadRepo.existsById(loadId)) throw new NotFoundException("Load not found");
        loadRepo.deleteById(loadId);
    }

    // Status transitions by BookingService

    public void markBooked(UUID loadId) {
        Load load = loadRepo.findById(loadId).orElseThrow(() -> new NotFoundException("Load not found"));
        load.setStatus(LoadStatus.BOOKED);
        loadRepo.save(load);
    }

    public void markCancelled(UUID loadId) {
        Load load = loadRepo.findById(loadId).orElseThrow(() -> new NotFoundException("Load not found"));
        load.setStatus(LoadStatus.CANCELLED);
        loadRepo.save(load);
    }

    public void revertToPosted(UUID loadId) {
        Load load = loadRepo.findById(loadId).orElseThrow(() -> new NotFoundException("Load not found"));
        load.setStatus(LoadStatus.POSTED);
        loadRepo.save(load);
    }

    private LoadDTO mapToDTO(Load load) {
        return LoadDTO.builder()
            .id(load.getId())
            .shipperId(load.getShipperId())
            .facility(new FacilityDTO(
                load.getFacility().getLoadingPoint(),
                load.getFacility().getUnloadingPoint(),
                load.getFacility().getLoadingDate(),
                load.getFacility().getUnloadingDate()
            ))
            .productType(load.getProductType())
            .truckType(load.getTruckType())
            .noOfTrucks(load.getNoOfTrucks())
            .weight(load.getWeight())
            .comment(load.getComment())
            .datePosted(load.getDatePosted())
            .status(load.getStatus())
            .build();
    }
}
