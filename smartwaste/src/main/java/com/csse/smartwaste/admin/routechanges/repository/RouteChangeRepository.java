package com.csse.smartwaste.admin.routechanges.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.csse.smartwaste.admin.routechanges.model.RouteChange;

@Repository
public interface RouteChangeRepository extends MongoRepository<RouteChange, String> {
}
