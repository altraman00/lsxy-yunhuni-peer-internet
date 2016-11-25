package com.lsxy.app.mc.vo;

import java.util.List;

/**
 * Created by tandy on 16/11/24.
 */
public class AreaServerVO {
    private String areaId;
    private String areaName;
    private List<AreaServerHostVO> hosts;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<AreaServerHostVO> getHosts() {
        return hosts;
    }

    public void setHosts(List<AreaServerHostVO> hosts) {
        this.hosts = hosts;
    }
}
