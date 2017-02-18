package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.callcenter.ConditionCreateInputDTO;
import com.lsxy.app.api.gateway.dto.callcenter.ConditionModifyInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.ConfController;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.framework.core.exceptions.api.AppServiceInvalidException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuws on 2016/11/14.
 */
@RestController
public class ConditionController extends AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(ConditionController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private ConditionService conditionService;

    @Autowired
    private AppService appService;

    @RequestMapping(value = "/{accountId}/callcenter/condition",method = RequestMethod.POST)
    public ApiGatewayResponse save(HttpServletRequest request, @PathVariable String accountId,
                                   @RequestHeader(value = "AppID") String appId,
                                   @Valid @RequestBody ConditionCreateInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("创建条件API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        Condition condition = new Condition();
        condition.setSubaccountId(getSubaccountId(request));
        condition.setWhereExpression(dto.getWhereExpression());
        condition.setSortExpression(dto.getSortExpression());
        condition.setPriority(dto.getPriority());
        condition.setQueueTimeout(dto.getQueueTimeout());
        condition.setFetchTimeout(dto.getFetchTimeout());
        condition.setRemark(dto.getRemark());
        condition = conditionService.save(app.getTenant().getId(),appId,condition);
        Map<String,String> result = new HashMap<>();
        result.put("conditionId",condition.getId());
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/callcenter/condition/{id}",method = RequestMethod.POST)
    public ApiGatewayResponse modify(HttpServletRequest request, @PathVariable String accountId,
                                   @RequestHeader(value = "AppID") String appId,
                                   @PathVariable String id,
                                   @Valid @RequestBody ConditionModifyInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("创建条件API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        Condition condition = new Condition();
        condition.setId(id);
        condition.setWhereExpression(dto.getWhereExpression());
        condition.setSortExpression(dto.getSortExpression());
        condition.setPriority(dto.getPriority());
        condition.setQueueTimeout(dto.getQueueTimeout());
        condition.setFetchTimeout(dto.getFetchTimeout());
        condition.setRemark(dto.getRemark());
        conditionService.save(app.getTenant().getId(),appId,condition);
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/callcenter/condition/{id}",method = RequestMethod.DELETE)
    public ApiGatewayResponse delete(HttpServletRequest request, @PathVariable String accountId,
                                     @RequestHeader(value = "AppID") String appId,
                                     @PathVariable String id) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("删除条件API参数,accountId={},appId={},id={}",accountId,appId,id);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        conditionService.delete(app.getTenant().getId(),app.getId(),id);
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/callcenter/condition",method = RequestMethod.GET)
    public ApiGatewayResponse conditions(HttpServletRequest request, @PathVariable String accountId,
                                       @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("条件列表API参数,accountId={},appId={},id={}",accountId,appId);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        return ApiGatewayResponse.success(conditionService.getAll(app.getTenant().getId(),app.getId()));
    }

    @RequestMapping(value = "/{accountId}/callcenter/condition/{id}",method = RequestMethod.GET)
    public ApiGatewayResponse findOne(HttpServletRequest request, @PathVariable String accountId,
                                      @RequestHeader(value = "AppID") String appId,
                                      @PathVariable String id) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("获取条件API参数,accountId={},appId={},id={}",accountId,appId,id);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        return ApiGatewayResponse.success(conditionService.findOne(app.getTenant().getId(),app.getId(),id));
    }
}
