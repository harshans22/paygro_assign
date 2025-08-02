package org.example.paygro_assign.service;

import lombok.RequiredArgsConstructor;
import org.example.paygro_assign.dto.BookingDTO;
import org.example.paygro_assign.enums.BookingStatus;
import org.example.paygro_assign.enums.LoadStatus;
import org.example.paygro_assign.exceptions.BadRequestException;
import org.example.paygro_assign.exceptions.NotFoundException;
import org.example.paygro_assign.model.Booking;
import org.example.paygro_assign.model.Load;
import org.example.paygro_assign.repository.BookingRepository;
import org.example.paygro_assign.repository.LoadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// com.cargopro.service.BookingService.java
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepo;
    private final LoadRepository loadRepo;
    private final LoadService loadService;

    @Transactional
    public BookingDTO createBooking(BookingDTO dto) {
        Load load = loadRepo.findById(dto.getLoadId())
            .orElseThrow(() -> new NotFoundException("Load not found"));
        if (load.getStatus() == LoadStatus.CANCELLED)
            throw new BadRequestException("Cannot book cancelled load");

        Booking booking = Booking.builder()
            .load(load)
            .transporterId(dto.getTransporterId())
            .proposedRate(dto.getProposedRate())
            .comment(dto.getComment())
            .status(BookingStatus.PENDING)
            .requestedAt(Instant.now())
            .build();
        Booking saved = bookingRepo.save(booking);
        // On first booking, mark load as BOOKED
        if (load.getStatus() == LoadStatus.POSTED)
            loadService.markBooked(load.getId());
        return mapToDTO(saved);
    }

    public BookingDTO getBooking(UUID bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new NotFoundException("Booking not found"));
        return mapToDTO(booking);
    }

    public List<BookingDTO> listBookings(UUID loadId, String transporterId, BookingStatus status) {
        List<Booking> bookings = bookingRepo.filterBookings(
            loadId, transporterId, status
        );
        return bookings.stream().map(this::mapToDTO).toList();
    }

    @Transactional
    public BookingDTO updateBooking(UUID bookingId, BookingDTO dto) {
        Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (dto.getProposedRate() != 0) {
            booking.setProposedRate(dto.getProposedRate());
        }
        if (dto.getComment() != null) {
            booking.setComment(dto.getComment());
        }
        if (dto.getStatus() == BookingStatus.ACCEPTED) {
            booking.setStatus(BookingStatus.ACCEPTED);
        } else if (dto.getStatus() == BookingStatus.REJECTED) {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepo.save(booking);

        // If accepted, update status
        if (booking.getStatus() == BookingStatus.ACCEPTED) {
            // Business logic: Only one can be accepted (optionally can enforce this)
        }
        // If all bookings are deleted or rejected, revert load to POSTED
        if (dto.getStatus() == BookingStatus.REJECTED) {
            long count = bookingRepo.countByLoadIdAndStatus(booking.getLoad().getId(), BookingStatus.PENDING)
                + bookingRepo.countByLoadIdAndStatus(booking.getLoad().getId(), BookingStatus.ACCEPTED);
            if (count == 0) {
                loadService.revertToPosted(booking.getLoad().getId());
            }
        }
        return mapToDTO(booking);
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new NotFoundException("Booking not found"));
        UUID loadId = booking.getLoad().getId();
        bookingRepo.delete(booking);
        // If all bookings are deleted, revert load to POSTED (unless CANCELLED)
        long otherBookings = bookingRepo.countByLoadId(loadId);
        if (otherBookings == 0) {
            Load load = loadRepo.findById(loadId).orElseThrow();
            if (load.getStatus() != LoadStatus.CANCELLED)
                loadService.revertToPosted(loadId);
        }
    }

    private BookingDTO mapToDTO(Booking booking) {
        return BookingDTO.builder()
            .id(booking.getId())
            .loadId(booking.getLoad().getId())
            .transporterId(booking.getTransporterId())
            .proposedRate(booking.getProposedRate())
            .comment(booking.getComment())
            .status(booking.getStatus())
            .requestedAt(booking.getRequestedAt())
            .build();
    }
}
