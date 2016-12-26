package com.lsxy.area.api;

public interface BusinessStateService {

    public void save(BusinessState state);

    public BusinessState get(String id);

    public void updateUserdata(String id,String userdata);

    public void updateResId(String id,String resId);

    public void updateCallBackUrl(String id,String callBackUrl);

    public void updateAreaId(String id,String areaId);

    public void updateLineGatewayId(String id,String lineGatewayId);

    public void updateClosed(String id,Boolean closed);

    public void updateInnerField(String id,String field,String value);

    public void deleteInnerField(String id,String field);

    public void delete(String id);
}
