package com.lsxy.area.api;

public interface BusinessStateService {

    public void save(BusinessState state);

    public BusinessState get(String id);

    public void delete(String id);
}
