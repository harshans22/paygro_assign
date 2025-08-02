package org.example.paygro_assign;

import org.example.paygro_assign.controller.BookingController;
import org.example.paygro_assign.dto.BookingDTO;
import org.example.paygro_assign.enums.BookingStatus;
import org.example.paygro_assign.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingControllerTest {
    @Mock private BookingService bookingService;
    @InjectMocks private BookingController bookingController;
    private MockMvc mockMvc;

    BookingDTO sampleBookingDTO(UUID loadId) {
        return BookingDTO.builder()
            .id(UUID.randomUUID())
            .loadId(loadId)
            .transporterId("t1")
            .proposedRate(100.0)
            .comment("ok")
            .status(BookingStatus.PENDING)
            .requestedAt(Instant.now())
            .build();
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void createBooking_returnsCreated() throws Exception {
        UUID loadId = UUID.randomUUID();
        BookingDTO dto = sampleBookingDTO(loadId);
        when(bookingService.createBooking(any())).thenReturn(dto);

        String json = """
        {
            "loadId": "%s",
            "transporterId": "t1",
            "proposedRate": 100.0
        }
        """.formatted(loadId);

        mockMvc.perform(post("/booking").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated());
    }

    @Test
    void getBooking_returnsOk() throws Exception {
        UUID bookingId = UUID.randomUUID();
        when(bookingService.getBooking(bookingId)).thenReturn(sampleBookingDTO(UUID.randomUUID()));

        mockMvc.perform(get("/booking/" + bookingId)).andExpect(status().isOk());
    }

    @Test
    void deleteBooking_returnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(bookingService).deleteBooking(id);

        mockMvc.perform(delete("/booking/" + id)).andExpect(status().isNoContent());
    }
}
