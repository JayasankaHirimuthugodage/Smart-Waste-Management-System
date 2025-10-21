package com.csse.smartwaste.admin.pickup.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.csse.smartwaste.admin.pickup.entity.SpecialPickup;
import com.csse.smartwaste.admin.pickup.repository.SpecialPickupRepository;
import com.csse.smartwaste.admin.pickup.service.SpecialPickupService;

/**
 * SRP: Handles pickup CRUD logic. OCP: Easily extendable for notifications or
 * IoT integrations later.
 */
@Service
public class SpecialPickupServiceImpl implements SpecialPickupService {

    private final SpecialPickupRepository repository;

    public SpecialPickupServiceImpl(SpecialPickupRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SpecialPickup> getAllRequests() {
        return repository.findAll();
    }

    @Override
    public SpecialPickup updateStatus(String id, String newStatus) {
        Optional<SpecialPickup> existing = repository.findById(id);
        if (existing.isPresent()) {
            SpecialPickup pickup = existing.get();
            pickup.setStatus(newStatus);
            return repository.save(pickup);
        }
        return null;
    }

    @Override
    public SpecialPickup createRequest(SpecialPickup request) {
        if (request.getStatus() == null) {
            request.setStatus("pending");
        }
        return repository.save(request);
    }
}
