package com.csse.smartwaste.admin.routechanges.service.impl;

import com.csse.smartwaste.admin.routechanges.model.RouteChange;
import com.csse.smartwaste.admin.routechanges.repository.RouteChangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RouteChangeServiceImplTest {

    @Mock
    private RouteChangeRepository repository;

    @InjectMocks
    private RouteChangeServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private RouteChange createMockRoute() {
        RouteChange r = new RouteChange();
        r.setId("r1");
        r.setRouteName("Route A");
        r.setArea("Colombo");
        r.setStatus("PENDING");
        r.setPriority("HIGH");
        r.setRequestedBy("System");
        r.setWasteVolumePerDay(900);
        r.setDateRequested(LocalDateTime.now());
        return r;
    }

    @Test
    void getAllRequests_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(createMockRoute()));

        List<RouteChange> result = service.getAllRequests();

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findAll();
    }

    @Test
    void createRequest_ShouldSetDefaultsAndSave() {
        RouteChange input = new RouteChange();
        when(repository.save(any(RouteChange.class))).thenAnswer(i -> i.getArgument(0));

        RouteChange saved = service.createRequest(input);

        assertThat(saved.getStatus()).isEqualTo("PENDING");
        assertThat(saved.getCurrentDistanceKm()).isEqualTo(14.5);
        assertThat(saved.getProposedDistanceKm()).isEqualTo(10.9);
        assertThat(saved.getPriority()).isEqualTo("HIGH");
        verify(repository, times(1)).save(any(RouteChange.class));
    }

    @Test
    void updateStatus_ShouldUpdateAndSave() {
        RouteChange existing = createMockRoute();
        when(repository.findById("r1")).thenReturn(Optional.of(existing));
        when(repository.save(any(RouteChange.class))).thenAnswer(i -> i.getArgument(0));

        RouteChange updated = service.updateStatus("r1", "APPROVED");

        assertThat(updated.getStatus()).isEqualTo("APPROVED");
        assertThat(updated.getEffectiveFrom()).isNotNull();
        verify(repository, times(1)).save(existing);
    }

    @Test
    void updateStatus_ShouldThrow_WhenNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, ()
                -> service.updateStatus("missing", "APPROVED")
        );
    }

    @Test
    void getHighWasteAreaSuggestions_ShouldReturnFilteredList() {
        RouteChange r1 = createMockRoute();
        r1.setWasteVolumePerDay(950);
        RouteChange r2 = createMockRoute();
        r2.setWasteVolumePerDay(500); // below threshold

        when(repository.findAll()).thenReturn(List.of(r1, r2));

        List<RouteChange> result = service.getHighWasteAreaSuggestions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getReason()).contains("High waste detected");
    }
}
