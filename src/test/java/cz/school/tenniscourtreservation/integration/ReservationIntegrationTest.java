package cz.school.tenniscourtreservation.integration;

import cz.school.tenniscourtreservation.model.Court;
import cz.school.tenniscourtreservation.model.SurfaceType;
import cz.school.tenniscourtreservation.model.User;
import cz.school.tenniscourtreservation.repository.CourtRepository;
import cz.school.tenniscourtreservation.repository.ReservationRepository;
import cz.school.tenniscourtreservation.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private User user;
    private Court court;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        courtRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setName("Integration User");
        user.setEmail("integration@example.com");
        user = userRepository.save(user);

        court = new Court();
        court.setName("Integration Court");
        court.setSurfaceType(SurfaceType.CLAY);
        court.setIndoor(false);
        court = courtRepository.save(court);
    }

    @Test
    void shouldCreateReservationAndStoreItInDatabase() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new CreateReservationJson(
                user.getId(),
                court.getId(),
                LocalDateTime.now().plusDays(1).withHour(18).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().plusDays(1).withHour(19).withMinute(0).withSecond(0).withNano(0)
        ));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        assertThat(reservationRepository.findAll()).hasSize(1);
        assertThat(reservationRepository.findAll().get(0).getTotalPrice()).isNotNull();
    }

    private record CreateReservationJson(
            Long userId,
            Long courtId,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
    }
}