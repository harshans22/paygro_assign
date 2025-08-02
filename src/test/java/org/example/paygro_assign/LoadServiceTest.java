package org.example.paygro_assign;

import org.example.paygro_assign.dto.FacilityDTO;
import org.example.paygro_assign.dto.LoadDTO;
import org.example.paygro_assign.enums.LoadStatus;
import org.example.paygro_assign.exceptions.BadRequestException;
import org.example.paygro_assign.exceptions.NotFoundException;
import org.example.paygro_assign.model.Facility;
import org.example.paygro_assign.model.Load;
import org.example.paygro_assign.repository.BookingRepository;
import org.example.paygro_assign.repository.LoadRepository;
import org.example.paygro_assign.service.LoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Instant;
import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoadServiceTest {
    @Mock
    private LoadRepository loadRepo;
    @Mock
    private BookingRepository bookingRepo;
    @InjectMocks
    private LoadService loadService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    Load sampleLoad(UUID id, LoadStatus status) {

        return Load.builder()
            .id(id)
            .shipperId("shipper1")
            .productType("Oil")
            .truckType("T1")
            .noOfTrucks(2)
            .weight(10.0)
            .facility(new Facility("A", "B", Instant.now(), Instant.now().plusSeconds(3600)))
            .comment("comment")
            .datePosted(Instant.now())
            .status(status)
            .build();
    }

    LoadDTO sampleLoadDTO() {
        return LoadDTO.builder()
            .shipperId("shipper1")
            .productType("Oil")
            .truckType("T1")
            .noOfTrucks(2)
            .weight(10.0)
            .comment("comment")
            .facility(new FacilityDTO("A", "B", Instant.now(), Instant.now().plusSeconds(3600)))
            .build();
    }

    @Test
    void createLoad_success() {
        LoadDTO dto = sampleLoadDTO();
        Load saved = sampleLoad(UUID.randomUUID(), LoadStatus.POSTED);
        when(loadRepo.save(any())).thenReturn(saved);
        LoadDTO result = loadService.createLoad(dto);
        assertEquals("shipper1", result.getShipperId());
        assertEquals(LoadStatus.POSTED, result.getStatus());
    }

    @Test
    void getLoad_found() {
        UUID id = UUID.randomUUID();
        when(loadRepo.findById(id)).thenReturn(Optional.of(sampleLoad(id, LoadStatus.BOOKED)));
        LoadDTO result = loadService.getLoad(id);
        assertEquals("shipper1", result.getShipperId());
    }

    @Test
    void getLoad_notFound() {
        UUID id = UUID.randomUUID();
        when(loadRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> loadService.getLoad(id));
    }

    @Test
    void listLoads_works() {
        Load load = sampleLoad(UUID.randomUUID(), LoadStatus.POSTED);
        Page<Load> loads = new PageImpl<>(List.of(load));
        when(loadRepo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(loads);
        Page<LoadDTO> page = loadService.listLoads("shipper1", null, null, 0, 10);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void updateLoad_foundAndValid() {
        UUID id = UUID.randomUUID();
        Load load = sampleLoad(id, LoadStatus.POSTED);
        LoadDTO dto = sampleLoadDTO();
        when(loadRepo.findById(id)).thenReturn(Optional.of(load));
        when(loadRepo.save(any())).thenReturn(load);

        LoadDTO updated = loadService.updateLoad(id, dto);
        assertEquals(dto.getProductType(), updated.getProductType());
    }

    @Test
    void updateLoad_cancelled_rejected() {
        UUID id = UUID.randomUUID();
        Load cancelled = sampleLoad(id, LoadStatus.CANCELLED);
        LoadDTO dto = sampleLoadDTO();
        when(loadRepo.findById(id)).thenReturn(Optional.of(cancelled));
        assertThrows(BadRequestException.class, () -> loadService.updateLoad(id, dto));
    }

    @Test
    void deleteLoad_exists() {
        UUID id = UUID.randomUUID();
        when(loadRepo.existsById(id)).thenReturn(true);
        loadService.deleteLoad(id);
        verify(loadRepo, times(1)).deleteById(id);
    }

    @Test
    void deleteLoad_notExists() {
        UUID id = UUID.randomUUID();
        when(loadRepo.existsById(id)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> loadService.deleteLoad(id));
    }

    @Test
    void markBooked_works() {
        UUID id = UUID.randomUUID();
        Load load = sampleLoad(id, LoadStatus.POSTED);
        when(loadRepo.findById(id)).thenReturn(Optional.of(load));
        loadService.markBooked(id);
        assertEquals(LoadStatus.BOOKED, load.getStatus());
    }

    @Test
    void markCancelled_works() {
        UUID id = UUID.randomUUID();
        Load load = sampleLoad(id, LoadStatus.BOOKED);
        when(loadRepo.findById(id)).thenReturn(Optional.of(load));
        loadService.markCancelled(id);
        assertEquals(LoadStatus.CANCELLED, load.getStatus());
    }

    @Test
    void revertToPosted_works() {
        UUID id = UUID.randomUUID();
        Load load = sampleLoad(id, LoadStatus.BOOKED);
        when(loadRepo.findById(id)).thenReturn(Optional.of(load));
        loadService.revertToPosted(id);
        assertEquals(LoadStatus.POSTED, load.getStatus());
    }
}

