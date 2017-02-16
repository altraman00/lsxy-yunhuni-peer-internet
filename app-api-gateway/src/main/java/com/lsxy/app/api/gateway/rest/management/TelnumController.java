package com.lsxy.app.api.gateway.rest.management;

import com.lsxy.app.api.gateway.dto.TelnumBindSubaccountInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by Administrator on 2017/2/15.
 */
@RestController
public class TelnumController extends AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(TelnumController.class);


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
        //TODO 查询号码列表
        return ApiGatewayResponse.success(null);
    }

    @RequestMapping(value = "/{accountId}/management/{id}/subaccount",method = RequestMethod.POST)
    public ApiGatewayResponse subaccount(HttpServletRequest request,
                                         @PathVariable String accountId,
                                         @RequestHeader(value = "AppID") String appId,
                                         @Valid @RequestBody TelnumBindSubaccountInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("号码绑定子账号API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        //TODO 绑定子账号
        return ApiGatewayResponse.success(null);
    }

    @RequestMapping(value = "/{accountId}/management/{id}/subaccount",method = RequestMethod.DELETE)
    public ApiGatewayResponse removeSubaccount(HttpServletRequest request,
                                         @PathVariable String accountId,
                                         @RequestHeader(value = "AppID") String appId,
                                         @RequestParam(defaultValue = "1") String subaccountId
    ) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("号码解绑子账号API参数,accountId={},appId={},subaccountId={}",accountId,appId,subaccountId);
        }
        //TODO 解绑子账号
        return ApiGatewayResponse.success(null);
    }
}

