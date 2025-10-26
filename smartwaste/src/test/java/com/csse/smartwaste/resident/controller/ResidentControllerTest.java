package com.csse.smartwaste.resident.controller;

import com.csse.smartwaste.common.model.Role;
import com.csse.smartwaste.login.entity.User;
import com.csse.smartwaste.common.util.UserValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResidentControllerTest {

    @Mock
    private UserValidationUtil userValidationUtil;

    @InjectMocks
    private ResidentController residentController;

    @Test
    void getDashboard_returnsPersonalizedData() {
        User user = new User();
        user.setUserId("u1");
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setRole(Role.Resident);

        when(userValidationUtil.validateUserAndRole("u1", Role.Resident)).thenReturn(user);

        ResponseEntity<Map<String, Object>> resp = residentController.getDashboard("u1");

        assertEquals(200, resp.getStatusCodeValue());
        Map<String, Object> body = resp.getBody();
        assertNotNull(body);
        assertEquals("u1", body.get("userId"));
        assertEquals("Bob", body.get("userName"));
        assertTrue(((String) body.get("message")).contains("Bob"));
    }

    @Test
    void getPayments_whenValidationFails_propagatesException() {
        when(userValidationUtil.validateUserAndRole("bad", Role.Resident)).thenThrow(new RuntimeException("not found"));

        assertThrows(RuntimeException.class, () -> residentController.getPayments("bad"));
    }
}
