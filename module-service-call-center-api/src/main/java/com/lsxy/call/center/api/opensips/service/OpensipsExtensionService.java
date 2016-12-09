package com.lsxy.call.center.api.opensips.service;

import com.lsxy.call.center.api.opensips.model.Location;

import java.util.List;

/**
 * Created by liups on 2016/11/24.
 */
public interface OpensipsExtensionService {
    void createExtension(String username,String password);
    void deleteExtension(String username);
    List<Location> getLocationsByUsername(String username);
}
