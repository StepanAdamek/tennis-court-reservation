package cz.school.tenniscourtreservation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.school.tenniscourtreservation.model.Court;
import cz.school.tenniscourtreservation.model.SurfaceType;
import cz.school.tenniscourtreservation.model.User;
import cz.school.tenniscourtreservation.repository.CourtRepository;
import cz.school.tenniscourtreservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserCourtIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtRepository courtRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        courtRepository.deleteAll();
    }

    @Test
    void shouldCreateUserAndStoreItInDatabase() throws Exception {
        User user = new User();
        user.setName("Stepan");
        user.setEmail("stepan.integration@example.com");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        assertThat(userRepository.findAll()).hasSize(1);
        assertThat(userRepository.findAll().get(0).getEmail())
                .isEqualTo("stepan.integration@example.com");
    }

    @Test
    void shouldCreateCourtAndStoreItInDatabase() throws Exception {
        Court court = new Court();
        court.setName("Integration Court");
        court.setSurfaceType(SurfaceType.HARD);
        court.setIndoor(true);

        mockMvc.perform(post("/api/courts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(court)))
                .andExpect(status().isCreated());

        assertThat(courtRepository.findAll()).hasSize(1);
        assertThat(courtRepository.findAll().get(0).getName())
                .isEqualTo("Integration Court");
    }

    @Test
    void shouldDeleteUserFromDatabase() throws Exception {
        User user = new User();
        user.setName("User To Delete");
        user.setEmail("delete.user@example.com");
        User savedUser = userRepository.save(user);

        mockMvc.perform(delete("/api/users/" + savedUser.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }

    @Test
    void shouldDeleteCourtFromDatabase() throws Exception {
        Court court = new Court();
        court.setName("Court To Delete");
        court.setSurfaceType(SurfaceType.GRASS);
        court.setIndoor(false);
        Court savedCourt = courtRepository.save(court);

        mockMvc.perform(delete("/api/courts/" + savedCourt.getId()))
                .andExpect(status().isNoContent());

        assertThat(courtRepository.findById(savedCourt.getId())).isEmpty();
    }
}