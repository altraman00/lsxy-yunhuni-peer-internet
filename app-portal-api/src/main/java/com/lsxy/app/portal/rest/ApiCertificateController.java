package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yuhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.yuhuni.api.exceptions.SkChangeCountLimitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 鉴权账号（凭证）RestApi接口
 * Created by liups on 2016/6/29.
 */
@RequestMapping("/rest/api_cert")
@RestController
public class ApiCertificateController extends AbstractRestController {

    @Autowired
    private ApiCertificateService apiCertificateService;

    /**
     * 查找当前用户所属租户的鉴权账号（凭证）
     * @throws Exception
     */
    @RequestMapping("/get")
    public RestResponse getApiCertificate() throws Exception{
        ApiCertificate apiCertificate = apiCertificateService.findApiCertificateByUserName(getCurrentAccountUserName());
        return RestResponse.success(apiCertificate);
    }

    /**
     * 更改鉴权账号（凭证）的secretKey
     * @throws Exception
     */
    @RequestMapping("/change_sk")
    public RestResponse changeSecretKey() throws MatchMutiEntitiesException {
        try{
            String secretKey = apiCertificateService.changeSecretKeyByUserName(getCurrentAccountUserName());
            return RestResponse.success(secretKey);
        }catch (SkChangeCountLimitException e){
            return RestResponse.failed("0000",e.getMessage());
        }
    }

}
