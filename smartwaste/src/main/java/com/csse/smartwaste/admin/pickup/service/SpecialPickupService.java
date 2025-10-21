package com.csse.smartwaste.admin.pickup.service;

import java.util.List;

import com.csse.smartwaste.admin.pickup.entity.SpecialPickup;

public interface SpecialPickupService {

    List<SpecialPickup> getAllRequests();

    SpecialPickup updateStatus(String id, String newStatus);

    SpecialPickup createRequest(SpecialPickup request);
}
