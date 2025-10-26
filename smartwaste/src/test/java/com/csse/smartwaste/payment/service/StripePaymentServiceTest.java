package com.csse.smartwaste.payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class StripePaymentServiceTest {

    private final StripePaymentService service = new StripePaymentService();

    @Test
    void processPayment_success_returnsIntentInfo() throws Exception {
        // Mock PaymentIntent static create
        PaymentIntent mockIntent = mock(PaymentIntent.class);
        Mockito.when(mockIntent.getId()).thenReturn("pi_test_123");
        Mockito.when(mockIntent.getClientSecret()).thenReturn("secret_abc");

        try (var mocked = Mockito.mockStatic(PaymentIntent.class)) {
            mocked.when(() -> PaymentIntent.create((com.stripe.param.PaymentIntentCreateParams) any())).thenReturn(mockIntent);

            Map<String, Object> result = service.processPayment(new BigDecimal("12.34"), "USD", "req-1");

            assertTrue((Boolean) result.get("success"));
            assertEquals("pi_test_123", result.get("paymentIntentId"));
            assertEquals("secret_abc", result.get("clientSecret"));
            assertEquals(new BigDecimal("12.34"), result.get("amount"));
        }
    }

    @Test
    void verifyPayment_succeeded_returnsTrue() throws StripeException {
        PaymentIntent mockIntent = mock(PaymentIntent.class);
        Mockito.when(mockIntent.getStatus()).thenReturn("succeeded");

        try (var mocked = Mockito.mockStatic(PaymentIntent.class)) {
            mocked.when(() -> PaymentIntent.retrieve("pi_ok")).thenReturn(mockIntent);

            assertTrue(service.verifyPayment("pi_ok"));
        }
    }

    @Test
    void verifyPayment_exception_returnsFalse() throws Exception {
        com.stripe.exception.StripeException mockEx = Mockito.mock(com.stripe.exception.StripeException.class);
        try (var mocked = Mockito.mockStatic(PaymentIntent.class)) {
            mocked.when(() -> PaymentIntent.retrieve("bad")).thenThrow(mockEx);
            assertFalse(service.verifyPayment("bad"));
        }
    }
}
