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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        when(reservationService.createReservation(any(CreateReservationRequest.class)))
                .thenReturn(reservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnAllReservations() throws Exception {
        when(reservationService.getAllReservations()).thenReturn(List.of(new Reservation()));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCancelReservationAndReturn200() throws Exception {
        Reservation reservation = new Reservation();

        when(reservationService.cancelReservationById(1L)).thenReturn(reservation);

        mockMvc.perform(put("/api/reservations/1/cancel"))
                .andExpect(status().isOk());
    }
}