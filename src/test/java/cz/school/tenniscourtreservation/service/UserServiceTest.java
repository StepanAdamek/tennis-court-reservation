package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.ResourceNotFoundException;
import cz.school.tenniscourtreservation.model.User;
import cz.school.tenniscourtreservation.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setName("Stepan");
        user.setEmail("stepan@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("stepan@example.com");

        verify(userRepository).findAll();
    }

    @Test
    void shouldCreateUser() {
        User user = new User();
        user.setName("Stepan");
        user.setEmail("stepan@example.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Stepan");
        savedUser.setEmail("stepan@example.com");

        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.createUser(user);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Stepan");
        assertThat(result.getEmail()).isEqualTo("stepan@example.com");

        verify(userRepository).save(user);
    }

    @Test
    void shouldDeleteUserWhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).existsById(99L);
    }
}