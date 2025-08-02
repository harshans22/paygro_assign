package org.example.paygro_assign.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.paygro_assign.dto.LoadDTO;
import org.example.paygro_assign.enums.LoadStatus;
import org.example.paygro_assign.service.LoadService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// com.cargopro.controller.LoadController.java
@RestController
@RequestMapping("/load")
@RequiredArgsConstructor
@Tag(name = "Load Management")
public class LoadController {
    private final LoadService loadService;

    @PostMapping
    @Operation(summary = "Create a new load")
    public ResponseEntity<LoadDTO> createLoad(@Valid @RequestBody LoadDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loadService.createLoad(dto));
    }

    @GetMapping
    @Operation(summary = "List loads with filtering and pagination")
    public Page<LoadDTO> listLoads(
        @RequestParam(required = false) String shipperId,
        @RequestParam(required = false) String truckType,
        @RequestParam(required = false) LoadStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return loadService.listLoads(shipperId, truckType, status, page, size);
    }

    @GetMapping("/{loadId}")
    @Operation(summary = "Get load details")
    public LoadDTO getLoad(@PathVariable UUID loadId) {
        return loadService.getLoad(loadId);
    }

    @PutMapping("/{loadId}")
    @Operation(summary = "Update load details")
    public LoadDTO updateLoad(@PathVariable UUID loadId, @Valid @RequestBody LoadDTO dto) {
        return loadService.updateLoad(loadId, dto);
    }

    @DeleteMapping("/{loadId}")
    @Operation(summary = "Delete load")
    public ResponseEntity<Void> deleteLoad(@PathVariable UUID loadId) {
        loadService.deleteLoad(loadId);
        return ResponseEntity.noContent().build();
    }
}
