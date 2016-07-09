package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 呼入号码管理
 * Created by zhangxb on 2016/7/1.
 */
@RequestMapping("/rest/res_rent/")
@RestController
public class ResourcesRentController extends AbstractRestController{

    @Autowired
    ResourcesRentService resourcesRentService;

    /**
     * 获取租户的呼入号码分页数据
     * @param pageNo
     * @param pageSize
     * @return
     * @throws MatchMutiEntitiesException
     */
    @RequestMapping("/list")
    public RestResponse pageList(Integer pageNo, Integer pageSize) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        //获取该租户下的所有号码信息
        Page<ResourcesRent> page = resourcesRentService.pageListByTenantId(userName,pageNo,pageSize);
        return RestResponse.success(page);
    }

}
