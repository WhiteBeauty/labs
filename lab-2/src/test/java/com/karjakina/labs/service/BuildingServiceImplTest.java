package com.karjakina.labs.service;

import com.karjakina.labs.dao.BuildingRepository;
import com.karjakina.labs.entity.BuildingEntity;
import com.karjakina.labs.exception.BuildingNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildingServiceImplTest {

    @Mock
    private BuildingRepository buildingRepository;

    private BuildingServiceImpl buildingService;

    @BeforeEach
    void setUp() {
        buildingService = new BuildingServiceImpl(buildingRepository);
    }

    // save

    @Test
    void save_shouldReturnIdFromRepository() {
        when(buildingRepository.save(any(BuildingEntity.class))).thenReturn(1);

        int id = buildingService.save("ул. Ленина, д. 1", 5);

        assertEquals(1, id);
        verify(buildingRepository, times(1)).save(any(BuildingEntity.class));
    }

    @Test
    void save_shouldPassCorrectFieldsToRepository() {
        when(buildingRepository.save(any(BuildingEntity.class))).thenReturn(42);

        buildingService.save("пр. Мира, д. 5", 10);

        ArgumentCaptor<BuildingEntity> captor = ArgumentCaptor.forClass(BuildingEntity.class);
        verify(buildingRepository).save(captor.capture());

        BuildingEntity captured = captor.getValue();
        assertEquals("пр. Мира, д. 5", captured.getAddress());
        assertEquals(10, captured.getFloors());
    }

    //  findById

    @Test
    void findById_shouldReturnBuilding_whenExists() {
        BuildingEntity expected = new BuildingEntity(1, "ул. Ленина, д. 1", 5);
        when(buildingRepository.findById(1)).thenReturn(expected);

        BuildingEntity result = buildingService.findById(1);

        assertEquals(expected, result);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(buildingRepository.findById(999)).thenReturn(null);

        assertThrows(BuildingNotFoundException.class, () -> buildingService.findById(999));
    }

    @Test
    void findById_exceptionMessage_shouldContainId() {
        when(buildingRepository.findById(7)).thenReturn(null);

        BuildingNotFoundException exception = assertThrows(
                BuildingNotFoundException.class,
                () -> buildingService.findById(7)
        );

        assertTrue(exception.getMessage().contains("7"));
    }

    // findByAddress

    @Test
    void findByAddress_shouldReturnBuilding_whenExists() {
        BuildingEntity building = new BuildingEntity(1, "ул. Садовая, д. 3", 4);
        when(buildingRepository.findAll()).thenReturn(List.of(building));

        BuildingEntity result = buildingService.findByAddress("ул. Садовая, д. 3");

        assertEquals(building, result);
    }

    @Test
    void findByAddress_shouldThrowException_whenNotFound() {
        when(buildingRepository.findAll()).thenReturn(List.of());

        assertThrows(BuildingNotFoundException.class,
                () -> buildingService.findByAddress("Несуществующий адрес"));
    }

    @Test
    void findByAddress_shouldThrowException_whenAddressDoesNotMatch() {
        BuildingEntity building = new BuildingEntity(1, "ул. Ленина, д. 1", 5);
        when(buildingRepository.findAll()).thenReturn(List.of(building));

        assertThrows(BuildingNotFoundException.class,
                () -> buildingService.findByAddress("ул. Пушкина, д. 10"));
    }

    // findAll

    @Test
    void findAll_shouldReturnAllBuildings() {
        List<BuildingEntity> buildings = List.of(
                new BuildingEntity(1, "ул. Ленина, д. 1", 5),
                new BuildingEntity(2, "пр. Мира, д. 42", 12)
        );
        when(buildingRepository.findAll()).thenReturn(buildings);

        List<BuildingEntity> result = buildingService.findAll();

        assertEquals(2, result.size());
        assertEquals(buildings, result);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoBuildingsExist() {
        when(buildingRepository.findAll()).thenReturn(List.of());

        List<BuildingEntity> result = buildingService.findAll();

        assertTrue(result.isEmpty());
    }

    // update

    @Test
    void update_shouldCallRepository_whenBuildingExists() {
        BuildingEntity building = new BuildingEntity(1, "ул. Ленина, д. 1", 9);
        when(buildingRepository.update(building)).thenReturn(true);

        // не должно бросить исключение
        assertDoesNotThrow(() -> buildingService.update(building));
        verify(buildingRepository, times(1)).update(building);
    }

    @Test
    void update_shouldThrowException_whenBuildingNotFound() {
        BuildingEntity building = new BuildingEntity(999, "Адрес", 1);
        when(buildingRepository.update(building)).thenReturn(false);

        assertThrows(BuildingNotFoundException.class, () -> buildingService.update(building));
    }

    // deleteById

    @Test
    void deleteById_shouldCallRepository() {
        buildingService.deleteById(1);

        verify(buildingRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteById_shouldNotThrow_whenBuildingDoesNotExist() {
        doNothing().when(buildingRepository).deleteById(999);

        assertDoesNotThrow(() -> buildingService.deleteById(999));
    }
}