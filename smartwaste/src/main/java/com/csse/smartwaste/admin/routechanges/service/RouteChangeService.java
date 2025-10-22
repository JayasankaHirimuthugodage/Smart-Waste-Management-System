package com.csse.smartwaste.admin.routechanges.service;

import java.util.List;

import com.csse.smartwaste.admin.routechanges.model.RouteChange;

public interface RouteChangeService {

    List<RouteChange> getAllRequests();

    RouteChange createRequest(RouteChange request);

    RouteChange updateStatus(String id, String status);

    List<RouteChange> getHighWasteAreaSuggestions(); // NEW
}
