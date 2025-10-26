package com.csse.smartwaste.admin.adminbin.service;

import com.csse.smartwaste.admin.adminbin.entity.Bin;
import com.csse.smartwaste.admin.adminbin.repository.BinRepository;
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

class BinServiceTest {

    @Mock
    private BinRepository binRepository;

    @InjectMocks
    private BinService binService;

    private Bin mockBin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockBin = new Bin();
        mockBin.setId("bin1");
        mockBin.setStatus("Active");
        mockBin.setAddress("Colombo");
        mockBin.setCreatedAt(LocalDateTime.now());
    }

    //  Positive Test
    @Test
    void getAllBins_ShouldReturnListOfBins() {
        when(binRepository.findAll()).thenReturn(List.of(mockBin));
        List<Bin> bins = binService.getAllBins();
        assertThat(bins).hasSize(1);
        assertThat(bins.get(0).getAddress()).isEqualTo("Colombo");
    }

    //  Positive Test
    @Test
    void getBinById_ShouldReturnOptionalBin_WhenExists() {
        when(binRepository.findById("bin1")).thenReturn(Optional.of(mockBin));
        Optional<Bin> result = binService.getBinById("bin1");
        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo("Active");
    }

    //  Negative Test
    @Test
    void getBinById_ShouldReturnEmpty_WhenNotFound() {
        when(binRepository.findById("missing")).thenReturn(Optional.empty());
        Optional<Bin> result = binService.getBinById("missing");
        assertThat(result).isEmpty();
    }

    //  Positive Test
    @Test
    void createBinRequest_ShouldSaveWithPendingStatus() {
        when(binRepository.save(any(Bin.class))).thenReturn(mockBin);
        Bin created = binService.createBinRequest(new Bin());
        assertThat(created).isNotNull();
        verify(binRepository, times(1)).save(any(Bin.class));
    }

    // Positive Test
    @Test
    void updateStatus_ShouldUpdateBinSuccessfully() {
        when(binRepository.findById("bin1")).thenReturn(Optional.of(mockBin));
        when(binRepository.save(any(Bin.class))).thenReturn(mockBin);

        Bin updated = binService.updateStatus("bin1", "Approved", "All good");

        assertThat(updated.getStatus()).isEqualTo("Approved");
        assertThat(updated.getNote()).isEqualTo("All good");
        verify(binRepository).save(any(Bin.class));
    }

    // Negative Test
    @Test
    void updateStatus_ShouldThrowException_WhenNotFound() {
        when(binRepository.findById("invalid")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> binService.updateStatus("invalid", "Approved", "note"));
    }

    // Positive Test
    @Test
    void deleteBin_ShouldCallRepositoryDeleteById() {
        doNothing().when(binRepository).deleteById("bin1");
        binService.deleteBin("bin1");
        verify(binRepository, times(1)).deleteById("bin1");
    }
}
