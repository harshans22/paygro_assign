package org.example.paygro_assign.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.paygro_assign.dto.BookingDTO;
import org.example.paygro_assign.enums.BookingStatus;
import org.example.paygro_assign.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// com.cargopro.controller.BookingController.java
@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@Tag(name = "Booking Management")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking")
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(dto));
    }

    @GetMapping
    @Operation(summary = "List bookings with filtering")
    public List<BookingDTO> listBookings(
        @RequestParam(required = false) UUID loadId,
        @RequestParam(required = false) String transporterId,
        @RequestParam(required = false) BookingStatus status
    ) {
        return bookingService.listBookings(loadId, transporterId, status);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking details")
    public BookingDTO getBooking(@PathVariable UUID bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @PutMapping("/{bookingId}")
    @Operation(summary = "Update booking")
    public BookingDTO updateBooking(@PathVariable UUID bookingId, @Valid @RequestBody BookingDTO dto) {
        return bookingService.updateBooking(bookingId, dto);
    }

    @DeleteMapping("/{bookingId}")
    @Operation(summary = "Delete booking")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}
