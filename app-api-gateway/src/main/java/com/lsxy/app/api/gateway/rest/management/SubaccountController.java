package com.lsxy.app.api.gateway.rest.management;

import com.lsxy.app.api.gateway.dto.*;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.framework.core.exceptions.api.AppServiceInvalidException;
import com.lsxy.framework.core.exceptions.api.IPNotInWhiteListException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.x509.CertificateSubjectName;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuws on 2017/2/15.
 */
@RestController
public class SubaccountController extends AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(SubaccountController.class);

    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;

    @Autowired
    private AppService appService;

    @Autowired
    private CertAccountQuotaService certAccountQuotaService;

    @RequestMapping(value = "/{accountId}/management/subaccount",method = RequestMethod.POST)
    public ApiGatewayResponse add(HttpServletRequest request,
                                  @PathVariable String accountId,
                                  @RequestHeader(value = "AppID") String appId,
                                  @Valid @RequestBody AddSubaccountInputDTO dto
                                  ) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("新增子账号API参数,accountId={},appId={},dto{}",accountId,appId,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        ApiCertificateSubAccount subAccount = new ApiCertificateSubAccount();
        subAccount.setAppId(appId);
        subAccount.setEnabled(1);
        subAccount.setCallbackUrl(dto.getCallbackUrl());
        subAccount.setRemark(dto.getRemark());
        subAccount = apiCertificateSubAccountService.createSubAccount(subAccount);
        return ApiGatewayResponse.success(subAccount);
    }

    @RequestMapping(value = "/{accountId}/management/subaccount/{id}",method = RequestMethod.PUT)
    public ApiGatewayResponse update(HttpServletRequest request,
                                         @PathVariable String accountId,
                                         @RequestHeader(value = "AppID") String appId,
                                        @PathVariable String id,
                                         @Valid @RequestBody UpdateSubaccountInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("修改子账号API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        ApiCertificateSubAccount subAccount = apiCertificateSubAccountService.findById(id);
        subAccount.setEnabled(dto.getEnabled());
        subAccount.setCallbackUrl(dto.getCallbackUrl());
        subAccount.setRemark(dto.getRemark());
        apiCertificateSubAccountService.updateSubAccount(subAccount);
        return ApiGatewayResponse.success(subAccount);
    }

    @RequestMapping(value = "/{accountId}/management/subaccount/{id}",method = RequestMethod.DELETE)
    public ApiGatewayResponse delete(HttpServletRequest request,
                                         @PathVariable String accountId,
                                         @RequestHeader(value = "AppID") String appId,
                                         @RequestParam(defaultValue = "1") String subaccountId
    ) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("删除子账号API参数,accountId={},appId={},subaccountId={}",accountId,appId,subaccountId);
        }
        String ip = WebUtils.getRemoteAddress(request);
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        apiCertificateSubAccountService.deleteSubAccount(app.getTenant().getId(),appId,subaccountId);
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/management/subaccount/{id}/quotas",method = RequestMethod.PUT)
    public ApiGatewayResponse setQuota(HttpServletRequest request,
                                       @PathVariable String accountId,
                                       @RequestHeader(value = "AppID") String appId,
                                       @PathVariable String id,
                                       @Valid @RequestBody SetSubaccountQuotaInputDTO dto
                                       ) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("设置子账号配额API参数,accountId={},appId={},subaccountId={}，dto={}",accountId,appId,id,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        List<CertAccountQuota> quotas = new ArrayList<>();
        if(dto.getQuotas()!=null && dto.getQuotas().size()>0){
            List<QuotaDTO> qs = dto.getQuotas();
            for(QuotaDTO q : qs){
                CertAccountQuota quota = new CertAccountQuota();
                quota.setType(q.getType());
                quota.setValue(q.getValue());
                quotas.add(quota);
            }
        }
        certAccountQuotaService.updateQuotas(id,quotas);
        return ApiGatewayResponse.success(true);
    }


    @RequestMapping(value = "/{accountId}/management/subaccount",method = RequestMethod.GET)
    public ApiGatewayResponse pagelist(HttpServletRequest request,
                                       @PathVariable String accountId,
                                       @RequestHeader(value = "AppID") String appId,
                                       @RequestParam(defaultValue = "1") Long pageNo,
                                       @RequestParam(defaultValue = "10") Long pageSize
    ) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("子账号列表API参数,accountId={},appId={},pageNo={},pageSize={}",accountId,appId,pageNo,pageSize);
        }
        String ip = WebUtils.getRemoteAddress(request);
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        Page<ApiCertificateSubAccount> pageDatas = apiCertificateSubAccountService
                .pageListWithNotQuota(appId,pageNo.intValue(),pageSize.intValue());

        return ApiGatewayResponse.success(null);
    }

    @RequestMapping(value = "/{accountId}/management/subaccount/{id}",method = RequestMethod.GET)
    public ApiGatewayResponse detail(HttpServletRequest request,
                                       @PathVariable String accountId,
                                       @RequestHeader(value = "AppID") String appId,
                                       @PathVariable String id
    ) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("子账号详情API参数,accountId={},appId={},id={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        ApiCertificateSubAccount subAccount = apiCertificateSubAccountService.findByIdWithQuota(id);

        return ApiGatewayResponse.success(null);
    }


}

