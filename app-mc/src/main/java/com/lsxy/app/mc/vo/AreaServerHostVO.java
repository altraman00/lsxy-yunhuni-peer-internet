package com.lsxy.app.mc.vo;

import java.util.List;

/**
 * Created by tandy on 16/11/24.
 */
public class AreaServerHostVO {
    private String hostName;
    private List<AreaNodeVO> nodes;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public List<AreaNodeVO> getNodes() {
        return nodes;
    }

    public void setNodes(List<AreaNodeVO> nodes) {
        this.nodes = nodes;
    }
}
