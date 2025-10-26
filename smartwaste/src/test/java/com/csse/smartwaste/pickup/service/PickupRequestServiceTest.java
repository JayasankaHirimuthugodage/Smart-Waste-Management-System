package com.csse.smartwaste.pickup.service;

import com.csse.smartwaste.common.exception.ResourceNotFoundException;
import com.csse.smartwaste.common.model.PaymentStatus;
import com.csse.smartwaste.common.model.PickupType;
import com.csse.smartwaste.common.model.WasteType;
import com.csse.smartwaste.login.entity.User;
import com.csse.smartwaste.pickup.service.NotificationService;
import com.csse.smartwaste.pickup.dto.PaymentRequestDTO;
import com.csse.smartwaste.pickup.dto.PickupRequestCreateDTO;
import com.csse.smartwaste.pickup.dto.PickupRequestResponseDTO;
import com.csse.smartwaste.pickup.entity.PickupRequest;
import com.csse.smartwaste.pickup.repository.PickupRequestRepository;
import com.csse.smartwaste.login.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PickupRequestServiceTest {

    @Mock
    private PickupRequestRepository pickupRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PickupRequestService pickupRequestService;

    @Test
    void calculateFees_baseGeneral_noWeight_regular() {
        PickupRequestCreateDTO dto = new PickupRequestCreateDTO();
        dto.setWasteType(WasteType.GENERAL);
        dto.setPickupType(PickupType.REGULAR);
        dto.setRewardPointsUsed(BigDecimal.ZERO);

        var calc = pickupRequestService.calculateFees(dto);

        assertNotNull(calc);
        assertEquals(new BigDecimal("10.00"), calc.getBaseAmount());
        assertEquals(BigDecimal.ZERO, calc.getUrgencyFee());
        assertEquals(new BigDecimal("10.00"), calc.getFinalAmount());
        assertTrue(calc.getCalculationBreakdown().contains("Final Amount"));
    }

    @Test
    void createPickupRequest_success_setsFieldsAndSendsNotification() {
        // Prepare user
        User user = new User();
        user.setUserId("user-1");
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPhone("+123456789");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(pickupRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PickupRequestCreateDTO create = new PickupRequestCreateDTO();
        create.setUserId("user-1");
        create.setWasteType(WasteType.GENERAL);
        create.setPickupType(PickupType.REGULAR);
        create.setPaymentMethod("Card");
        create.setRewardPointsUsed(BigDecimal.ZERO);

        PickupRequestResponseDTO resp = pickupRequestService.createPickupRequest(create);

        assertNotNull(resp.getRequestId());
        assertEquals("Alice", resp.getUserName());
        assertEquals("alice@example.com", resp.getUserEmail());
        assertEquals(PaymentStatus.COMPLETED, resp.getPaymentStatus());

        // Verify notification sent
        verify(notificationService, times(1)).sendPickupRequestConfirmation(any(PickupRequest.class));
    }

    @Test
    void getPickupRequestById_notFound_throws() {
        when(pickupRequestRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pickupRequestService.getPickupRequestById("missing"));
    }

    @Test
    void processPayment_card_success_updatesStatus() {
        PickupRequest pr = new PickupRequest();
        pr.setRequestId("r1");
        pr.setFinalAmount(new BigDecimal("20.00"));
        pr.setStatus(null);

        when(pickupRequestRepository.findById("r1")).thenReturn(Optional.of(pr));
        when(pickupRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentRequestDTO payment = new PaymentRequestDTO();
        payment.setPaymentMethod("Card");
        payment.setAmount(new BigDecimal("20.00"));
        payment.setPaymentReference("ref-123");

        var resp = pickupRequestService.processPayment("r1", payment);

        assertEquals(PaymentStatus.COMPLETED, resp.getPaymentStatus());
        assertEquals("ref-123", resp.getPaymentReference());
        verify(notificationService, times(1)).sendPaymentConfirmation(any(PickupRequest.class));
    }

    @Test
    void deletePickupRequest_illegalState_throws() {
        PickupRequest pr = new PickupRequest();
        pr.setRequestId("r2");
        pr.setStatus(com.csse.smartwaste.common.model.PickupStatus.COMPLETED);

        when(pickupRequestRepository.findById("r2")).thenReturn(Optional.of(pr));

        assertThrows(IllegalStateException.class, () -> pickupRequestService.deletePickupRequest("r2"));
    }
}
