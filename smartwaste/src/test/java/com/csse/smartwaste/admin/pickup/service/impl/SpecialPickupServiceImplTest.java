package com.csse.smartwaste.admin.pickup.service.impl;

import com.csse.smartwaste.admin.pickup.entity.SpecialPickup;
import com.csse.smartwaste.admin.pickup.repository.SpecialPickupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpecialPickupServiceImplTest {

    @Mock
    private SpecialPickupRepository repository;

    @InjectMocks
    private SpecialPickupServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private SpecialPickup createMockPickup() {
        SpecialPickup pickup = new SpecialPickup();
        pickup.setId("p1");
        pickup.setName("John Doe");
        pickup.setArea("Colombo");
        pickup.setDate("2025-11-01");
        pickup.setType("Plastic");
        pickup.setStatus("pending");
        return pickup;
    }

    @Test
    void getAllRequests_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(createMockPickup()));

        List<SpecialPickup> result = service.getAllRequests();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        verify(repository, times(1)).findAll();
    }

    @Test
    void createRequest_ShouldSetDefaultStatus_WhenStatusNull() {
        SpecialPickup pickup = new SpecialPickup();
        pickup.setName("New User");
        pickup.setArea("Kandy");
        pickup.setDate("2025-11-02");
        pickup.setType("Glass");
        pickup.setStatus(null);

        when(repository.save(any(SpecialPickup.class))).thenAnswer(i -> i.getArgument(0));

        SpecialPickup result = service.createRequest(pickup);

        assertThat(result.getStatus()).isEqualTo("pending");
        verify(repository, times(1)).save(any(SpecialPickup.class));
    }

    @Test
    void createRequest_ShouldPreserveExistingStatus() {
        SpecialPickup pickup = createMockPickup();
        pickup.setStatus("approved");

        when(repository.save(any(SpecialPickup.class))).thenReturn(pickup);

        SpecialPickup result = service.createRequest(pickup);

        assertThat(result.getStatus()).isEqualTo("approved");
    }

    @Test
    void updateStatus_ShouldUpdate_WhenPickupExists() {
        SpecialPickup existing = createMockPickup();
        when(repository.findById("p1")).thenReturn(Optional.of(existing));
        when(repository.save(any(SpecialPickup.class))).thenAnswer(i -> i.getArgument(0));

        SpecialPickup updated = service.updateStatus("p1", "approved");

        assertThat(updated.getStatus()).isEqualTo("approved");
        verify(repository, times(1)).save(existing);
    }

    @Test
    void updateStatus_ShouldReturnNull_WhenPickupNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        SpecialPickup result = service.updateStatus("missing", "approved");

        assertThat(result).isNull();
        verify(repository, never()).save(any(SpecialPickup.class));
    }
}
