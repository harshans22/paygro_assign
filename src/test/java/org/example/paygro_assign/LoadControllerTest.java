package org.example.paygro_assign;

import org.example.paygro_assign.controller.LoadController;
import org.example.paygro_assign.dto.FacilityDTO;
import org.example.paygro_assign.dto.LoadDTO;
import org.example.paygro_assign.enums.LoadStatus;
import org.example.paygro_assign.service.LoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoadControllerTest {
    @Mock
    private LoadService loadService;

    @InjectMocks
    private LoadController loadController;

    private MockMvc mockMvc;

    LoadDTO sampleLoadDTO(UUID id) {
        FacilityDTO facility = new FacilityDTO("A", "B", Instant.now(), Instant.now().plusSeconds(3600));
        return LoadDTO.builder().id(id).shipperId("s1").truckType("T1").noOfTrucks(1).weight(2.0)
            .facility(facility).productType("Something").status(LoadStatus.POSTED).build();
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loadController).build();
    }

    @Test
    void createLoad_shouldReturnCreated() throws Exception {
        LoadDTO dto = sampleLoadDTO(null);
        when(loadService.createLoad(any())).thenReturn(dto);

        String json = """
            {
            "shipperId":"s1",
            "facility":{"loadingPoint":"A","unloadingPoint":"B","loadingDate":"2022-01-01T10:00:00Z","unloadingDate":"2022-01-01T14:00:00Z"},
            "productType":"Something",
            "truckType":"T1",
            "noOfTrucks":1,
            "weight":2.0
            }""";

        mockMvc.perform(post("/load").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated());
    }

    @Test
    void getLoad_shouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(loadService.getLoad(id)).thenReturn(sampleLoadDTO(id));

        mockMvc.perform(get("/load/" + id)).andExpect(status().isOk());
    }

    @Test
    void deleteLoad_shouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(loadService).deleteLoad(id);
        mockMvc.perform(delete("/load/" + id)).andExpect(status().isNoContent());
    }
}
