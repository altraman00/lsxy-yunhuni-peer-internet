package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToPublic;
import com.lsxy.yunhuni.api.config.model.LineGatewayVO;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/25.
 */
public interface LineGatewayToPublicService extends BaseService<LineGatewayToPublic> {
    int getMaxPriority();
    int findByLindId(String lindId);
    Page<LineGatewayToPublic> getPage(Integer pageNo, Integer pageSize, String operator, String isThrough, String status, String isPublicLine, String order);

    List<LineGatewayVO> findAllLineGatewayByAreaId(String areaId);
    List<LineGateway> findAllLineGateway();

    void deleteLine(String line);

    /**
     * 加入全局线路
     */
    void addPublic(String id);
    /**
     * 移除全局线路
     */
    void removePublic(String id);

    int upPriority(int o1,int o2,String line);

}
