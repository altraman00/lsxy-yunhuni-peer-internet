package com.lsxy.app.portal.rest.number;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 呼入号码管理
 * Created by zhangxb on 2016/7/1.
 */
@RequestMapping("/rest/res_rent")
@RestController
public class ResourcesRentController extends AbstractRestController{

    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    ResourceTelenumService resourceTelenumService;
    /**
     * 获取租户的呼入号码分页数据
     * @param pageNo
     * @param pageSize
     * @return
     * @throws MatchMutiEntitiesException
     */
    @RequestMapping("/list")
    public RestResponse pageList(Integer pageNo, Integer pageSize)   {
        String userName = getCurrentAccountUserName();
        //获取该租户下的所有号码信息
        Page<ResourcesRent> page = resourcesRentService.pageListByTenantId(userName,pageNo,pageSize);
        return RestResponse.success(page);
    }
    /**
     * 根据id释放手机号码
     * @param id 租户号码id
     * @return
     */
    @RequestMapping("/release")
    public RestResponse release(String id)   {
        ResourcesRent resourcesRent = resourcesRentService.findById(id);
        resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
        resourcesRentService.save(resourcesRent);
        ResourceTelenum resourceTelenum =  resourcesRent.getResourceTelenum();
        resourceTelenum.setTenant(null);
        resourceTelenum.setStatus(ResourceTelenum.STATUS_FREE);
        resourceTelenumService.save(resourceTelenum);
        return RestResponse.success(resourcesRent);
    }
    @RequestMapping("/by_app/{appId}")
    public RestResponse getByAppId(@PathVariable String appId){
        ResourcesRent rent = null;
        List<ResourcesRent> rents = resourcesRentService.findByAppId(appId);
        if(rents != null && rents.size()>0){
            rent = rents.get(0);
        }
        return RestResponse.success(rent);
    }


}
