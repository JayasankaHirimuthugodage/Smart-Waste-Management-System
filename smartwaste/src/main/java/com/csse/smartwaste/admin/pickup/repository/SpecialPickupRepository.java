package com.csse.smartwaste.admin.pickup.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.csse.smartwaste.admin.pickup.entity.SpecialPickup;

public interface SpecialPickupRepository extends MongoRepository<SpecialPickup, String> {

    List<SpecialPickup> findByStatus(String status);
}
