package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.ResourceNotFoundException;
import cz.school.tenniscourtreservation.model.Court;
import cz.school.tenniscourtreservation.model.SurfaceType;
import cz.school.tenniscourtreservation.repository.CourtRepository;
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
class CourtServiceTest {

    @Mock
    private CourtRepository courtRepository;

    @InjectMocks
    private CourtServiceImpl courtService;

    @Test
    void shouldReturnAllCourts() {
        Court court = new Court();
        court.setId(1L);
        court.setName("Central Court");
        court.setSurfaceType(SurfaceType.CLAY);
        court.setIndoor(false);

        when(courtRepository.findAll()).thenReturn(List.of(court));

        List<Court> result = courtService.getAllCourts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Central Court");
        assertThat(result.get(0).getSurfaceType()).isEqualTo(SurfaceType.CLAY);

        verify(courtRepository).findAll();
    }

    @Test
    void shouldCreateCourt() {
        Court court = new Court();
        court.setName("Central Court");
        court.setSurfaceType(SurfaceType.CLAY);
        court.setIndoor(false);

        Court savedCourt = new Court();
        savedCourt.setId(1L);
        savedCourt.setName("Central Court");
        savedCourt.setSurfaceType(SurfaceType.CLAY);
        savedCourt.setIndoor(false);

        when(courtRepository.save(court)).thenReturn(savedCourt);

        Court result = courtService.createCourt(court);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Central Court");
        assertThat(result.getSurfaceType()).isEqualTo(SurfaceType.CLAY);

        verify(courtRepository).save(court);
    }

    @Test
    void shouldDeleteCourtWhenCourtExists() {
        when(courtRepository.existsById(1L)).thenReturn(true);

        courtService.deleteCourt(1L);

        verify(courtRepository).existsById(1L);
        verify(courtRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCourt() {
        when(courtRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> courtService.deleteCourt(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Court not found");

        verify(courtRepository).existsById(99L);
    }
}