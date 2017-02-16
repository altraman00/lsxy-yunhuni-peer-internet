package com.lsxy.app.api.gateway.rest.management;

import com.lsxy.app.api.gateway.dto.ResourceTelenumOutput;
import com.lsxy.app.api.gateway.dto.TelnumBindSubaccountInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.framework.core.exceptions.api.IPNotInWhiteListException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */
@RestController
public class TelnumController extends AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(TelnumController.class);


    @Autowired
    private ResourcesRentService resourcesRentService;

    @Autowired
    private ResourceTelenumService resourceTelenumService;

    @Autowired
    private AppService appService;

    @RequestMapping(value = "/{accountId}/management/telnum",method = RequestMethod.GET)
    public ApiGatewayResponse pagelist(HttpServletRequest request,
                                       @PathVariable String accountId,
                                       @RequestHeader(value = "AppID") String appId,
                                       @RequestParam(defaultValue = "1") Long pageNo,
                                       @RequestParam(defaultValue = "10") Long pageSize
                                       ) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("号码列表API参数,accountId={},appId={},pageNo={},pageSize={}",accountId,appId,pageNo,pageSize);
        }
        String ip = WebUtils.getRemoteAddress(request);
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        Page<ResourcesRent> pageDatas = resourcesRentService
                .pageListByTenantId(app.getTenant().getId(),pageNo.intValue(),pageSize.intValue());

        List<ResourceTelenumOutput> data = new ArrayList<ResourceTelenumOutput>(pageDatas.getPageSize());

        Page<ResourceTelenumOutput> result = new Page<ResourceTelenumOutput>(pageDatas.getStartIndex()
                ,pageDatas.getTotalPageCount(),pageDatas.getPageSize(),data);

        if(pageDatas.getResult()!=null && pageDatas.getResult().size()>0){
            List<ResourcesRent> rents= pageDatas.getResult();
            for (ResourcesRent rent:rents) {
                ResourceTelenum resource = rent.getResourceTelenum();
                ResourceTelenumOutput resourceTelenum=new ResourceTelenumOutput();
                resourceTelenum.setId(resource.getId());
                resourceTelenum.setAppId(resource.getAppId());
                resourceTelenum.setSubaccountId(resource.getSubaccountId());
                resourceTelenum.setTelnum(resource.getTelNumber());
                data.add(resourceTelenum);
            }
        }
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/management/telnum/{id}/subaccount",method = RequestMethod.POST)
    public ApiGatewayResponse subaccount(HttpServletRequest request,
                                         @PathVariable String accountId,
                                         @RequestHeader(value = "AppID") String appId,
                                         @PathVariable String id,
                                         @Valid @RequestBody TelnumBindSubaccountInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("号码绑定子账号API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        List<String> nums = new ArrayList<>();
        nums.add(resourceTelenum.getTelNumber());
        resourcesRentService.bindNumToSubaccount(app,nums,dto.getSubaccountId());
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/management/{id}/subaccount",method = RequestMethod.DELETE)
    public ApiGatewayResponse removeSubaccount(HttpServletRequest request,
                                           @PathVariable String accountId,
                                           @RequestHeader(value = "AppID") String appId,
                                           @PathVariable String id,
                                            @RequestParam(defaultValue = "1") String subaccountId
    ) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("号码解绑子账号API参数,accountId={},appId={},subaccountId={}",accountId,appId,subaccountId);
        }
        String ip = WebUtils.getRemoteAddress(request);
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        resourceTelenumService.subaccountUnbindNum(app.getTenant().getId(),appId,subaccountId,id);
        return ApiGatewayResponse.success(true);
    }
}

