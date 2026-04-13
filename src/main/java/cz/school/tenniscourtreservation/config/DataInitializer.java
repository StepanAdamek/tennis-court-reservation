package cz.school.tenniscourtreservation.config;

import cz.school.tenniscourtreservation.model.Court;
import cz.school.tenniscourtreservation.model.SurfaceType;
import cz.school.tenniscourtreservation.model.User;
import cz.school.tenniscourtreservation.repository.CourtRepository;
import cz.school.tenniscourtreservation.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, CourtRepository courtRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User user = new User();
                user.setName("Stepan");
                user.setEmail("stepan@example.com");
                userRepository.save(user);
            }

            if (courtRepository.count() == 0) {
                Court court = new Court();
                court.setName("Court 1");
                court.setSurfaceType(SurfaceType.CLAY);
                court.setIndoor(false);
                courtRepository.save(court);
            }
        };
    }
}