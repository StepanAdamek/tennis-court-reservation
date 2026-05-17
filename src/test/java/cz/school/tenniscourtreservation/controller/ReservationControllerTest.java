package cz.school.tenniscourtreservation.controller;

import cz.school.tenniscourtreservation.dto.CreateReservationRequest;
import cz.school.tenniscourtreservation.model.Reservation;
import cz.school.tenniscourtreservation.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("removal")
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateReservationAndReturn201() throws Exception {

        CreateReservationRequest request = new CreateReservationRequest();
        request.setUserId(1L);
        request.setCourtId(1L);
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationService.createReservation(any(CreateReservationRequest.class)))
                .thenReturn(reservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(reservationService).createReservation(any(CreateReservationRequest.class));
    }

    @Test
    void shouldReturnAllReservations() throws Exception {

        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationService.getAllReservations())
                .thenReturn(List.of(reservation));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(reservationService).getAllReservations();
    }

    @Test
    void shouldCancelReservationAndReturn200() throws Exception {

        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationService.cancelReservationById(1L))
                .thenReturn(reservation);

        mockMvc.perform(put("/api/reservations/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(reservationService).cancelReservationById(1L);
    }
}
