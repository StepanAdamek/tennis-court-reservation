package cz.school.tenniscourtreservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.school.tenniscourtreservation.model.Court;
import cz.school.tenniscourtreservation.model.SurfaceType;
import cz.school.tenniscourtreservation.service.CourtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("removal")
@WebMvcTest(CourtController.class)
class CourtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourtService courtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllCourts() throws Exception {

        Court court = new Court();
        court.setId(1L);
        court.setName("Central Court");
        court.setSurfaceType(SurfaceType.CLAY);

        when(courtService.getAllCourts())
                .thenReturn(List.of(court));

        mockMvc.perform(get("/api/courts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Central Court"));

        verify(courtService).getAllCourts();
    }

    @Test
    void shouldCreateCourtAndReturn201() throws Exception {

        Court court = new Court();
        court.setId(1L);
        court.setName("Central Court");
        court.setSurfaceType(SurfaceType.CLAY);

        when(courtService.createCourt(any(Court.class)))
                .thenReturn(court);

        mockMvc.perform(post("/api/courts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(court)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Central Court"));

        verify(courtService).createCourt(any(Court.class));
    }

    @Test
    void shouldDeleteCourtAndReturn204() throws Exception {

        mockMvc.perform(delete("/api/courts/1"))
                .andExpect(status().isNoContent());

        verify(courtService).deleteCourt(1L);
    }
}
