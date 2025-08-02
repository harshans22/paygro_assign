package org.example.paygro_assign;

import org.example.paygro_assign.dto.BookingDTO;
import org.example.paygro_assign.enums.BookingStatus;
import org.example.paygro_assign.enums.LoadStatus;
import org.example.paygro_assign.exceptions.BadRequestException;
import org.example.paygro_assign.exceptions.NotFoundException;
import org.example.paygro_assign.model.Booking;
import org.example.paygro_assign.model.Facility;
import org.example.paygro_assign.model.Load;
import org.example.paygro_assign.repository.BookingRepository;
import org.example.paygro_assign.repository.LoadRepository;
import org.example.paygro_assign.service.BookingService;
import org.example.paygro_assign.service.LoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepo;
    @Mock
    private LoadRepository loadRepo;
    @Mock
    private LoadService loadService;
    @InjectMocks
    private BookingService bookingService;

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

    Booking sampleBooking(UUID id, Load load, BookingStatus status) {
        return Booking.builder()
            .id(id)
            .load(load)
            .transporterId("trans1")
            .proposedRate(100.0)
            .comment("Ok")
            .status(status)
            .requestedAt(Instant.now())
            .build();
    }

    BookingDTO sampleBookingDTO(UUID loadId) {
        return BookingDTO.builder()
            .loadId(loadId)
            .transporterId("trans1")
            .proposedRate(100.0)
            .comment("Ok")
            .build();
    }

    @Test
    void createBooking_success_POSTED_to_BOOKED() {
        UUID loadId = UUID.randomUUID();
        Load load = sampleLoad(loadId, LoadStatus.POSTED);
        Booking booking = sampleBooking(UUID.randomUUID(), load, BookingStatus.PENDING);
        when(loadRepo.findById(loadId)).thenReturn(Optional.of(load));
        when(bookingRepo.save(any())).thenReturn(booking);

        BookingDTO dto = sampleBookingDTO(loadId);
        BookingDTO result = bookingService.createBooking(dto);
        verify(loadService, times(1)).markBooked(loadId);
        assertEquals("trans1", result.getTransporterId());
    }

    @Test
    void createBooking_CANNOT_book_CANCELLED() {
        UUID loadId = UUID.randomUUID();
        Load load = sampleLoad(loadId, LoadStatus.CANCELLED);
        when(loadRepo.findById(loadId)).thenReturn(Optional.of(load));
        BookingDTO dto = sampleBookingDTO(loadId);
        assertThrows(BadRequestException.class, () -> bookingService.createBooking(dto));
    }

    @Test
    void getBooking_found() {
        UUID bookingId = UUID.randomUUID();
        Load load = sampleLoad(UUID.randomUUID(), LoadStatus.BOOKED);
        Booking booking = sampleBooking(bookingId, load, BookingStatus.PENDING);
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));
        BookingDTO res = bookingService.getBooking(bookingId);
        assertEquals("trans1", res.getTransporterId());
    }

    @Test
    void getBooking_notFound() {
        UUID bookingId = UUID.randomUUID();
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(bookingId));
    }

    @Test
    void listBookings_filters() {
        UUID loadId = UUID.randomUUID();
        List<Booking> r = List.of(sampleBooking(UUID.randomUUID(), sampleLoad(loadId, LoadStatus.BOOKED), BookingStatus.ACCEPTED));
        when(bookingRepo.filterBookings(any(), any(), any())).thenReturn(r);
        List<BookingDTO> list = bookingService.listBookings(loadId, null, BookingStatus.ACCEPTED);
        assertEquals(1, list.size());
    }

    @Test
    void updateBooking_ACCEPTED_statusTransition() {
        UUID bookingId = UUID.randomUUID();
        Load load = sampleLoad(UUID.randomUUID(), LoadStatus.BOOKED);
        Booking booking = sampleBooking(bookingId, load, BookingStatus.PENDING);
        BookingDTO dto = sampleBookingDTO(load.getId());
        dto.setStatus(BookingStatus.ACCEPTED);

        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any())).thenReturn(booking);
        BookingDTO returned = bookingService.updateBooking(bookingId, dto);

        assertEquals(BookingStatus.ACCEPTED, returned.getStatus());
    }

    @Test
    void updateBooking_REJECTED_revertToPostedIfNone() {
        UUID bookingId = UUID.randomUUID();
        Load load = sampleLoad(UUID.randomUUID(), LoadStatus.BOOKED);
        Booking booking = sampleBooking(bookingId, load, BookingStatus.PENDING);
        BookingDTO dto = sampleBookingDTO(load.getId());
        dto.setStatus(BookingStatus.REJECTED);

        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any())).thenReturn(booking);
        when(bookingRepo.countByLoadIdAndStatus(any(), any())).thenReturn(0L);

        BookingDTO returned = bookingService.updateBooking(bookingId, dto);

        verify(loadService, times(1)).revertToPosted(load.getId());
    }

    @Test
    void deleteBooking_lastBookingRevertsLoad() {
        UUID bookingId = UUID.randomUUID();
        UUID loadId = UUID.randomUUID();
        Load load = sampleLoad(loadId, LoadStatus.BOOKED);
        Booking booking = sampleBooking(bookingId, load, BookingStatus.PENDING);

        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepo.countByLoadId(loadId)).thenReturn(0L);
        when(loadRepo.findById(loadId)).thenReturn(Optional.of(load));

        bookingService.deleteBooking(bookingId);
        verify(loadService, times(1)).revertToPosted(loadId);
        verify(bookingRepo).delete(booking);
    }
}
