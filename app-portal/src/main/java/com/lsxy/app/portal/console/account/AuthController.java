package com.lsxy.app.portal.console.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.tenant.model.RealnameCorp;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhangxb on 2016/6/24.
 * 实名认证
 */
@Controller
@RequestMapping("/console/account/auth")
public class AuthController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private OSSService ossService;
    /**
     * 实名认证首页
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(@RequestParam(value = "type",required = false) String type,HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        RestResponse<HashMap> restResponse = findAuthInfo(request);
        HashMap hs = restResponse.getData();
        int authStatus  = Integer.valueOf((hs.get("status")+""));
        String resultUrl = "/console/account/auth/sucess";//默认成功页面
        if(authStatus==Tenant.AUTH_ONESELF_FAIL||authStatus==Tenant.AUTH_COMPANY_FAIL){
            if (StringUtil.isNotEmpty(type)&&"fail".equals(type)) {//失败后重新认证
                resultUrl = "/console/account/auth/index";
            }
        }
        if(authStatus==Tenant.AUTH_ONESELF_SUCCESS||authStatus==Tenant.AUTH_UPGRADE_FAIL) {
            if (StringUtil.isNotEmpty(type)&&"upgrade".equals(type)) {//个人认证  升级到企业认证
                mav.addObject("upgrade", true);//个人认证  升级到企业认证
                resultUrl = "/console/account/auth/index";
            }
        }
        if(authStatus==Tenant.AUTH_NO){//尚未实名制
            resultUrl = "/console/account/auth/index";
        }
        mav.addAllObjects(hs);
        mav.setViewName(resultUrl);
        return mav;
    }

    /**
     * 获取用户是否实名认证
     * @return
     */
    @RequestMapping(value = "/is_real_auth",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse isRealAuth(HttpServletRequest request){
        RestResponse response = findAuthStatus(request);
        RestResponse result;
        if(response.isSuccess() && response.getData() != null){
            Map data = (Map) response.getData();
            int authStatus  = Integer.valueOf((data.get("status")+""));
            result = RestResponse.success(authStatus);
        }else{
            result = RestResponse.failed("1111","无法获取用户认证信息");
        }
        return result;
    }
    /**
     * 获取后台状态的rest请求方法
     * @return
     */
    private RestResponse findAuthStatus(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/account/auth/find_auth_status";
        return  RestRequest.buildSecurityRequest(token).get(uri,HashMap.class);
    }
    /**
     * 上次文件方法
     * @param file
     * @return
     */
    private String UploadFile(String tenantId,MultipartFile file) throws IOException{
        return ossService.uploadFile(tenantId,"realname_auth",file);
    }

    /**
     * 实名认证方法
     * @param request
     * @param authVo
     * @param type
     * @param multipartfiles
     * @return
     */
    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public ModelAndView edit(@RequestParam(value = "upgrade",required = false) String upgrade,HttpServletRequest request,AuthVo authVo,String type, @RequestParam("file") MultipartFile[] multipartfiles) throws IOException{

        String tenantId = this.getCurrentUser(request).getTenantId();

        //对上传文件进行处理
        if (null != multipartfiles && multipartfiles.length > 0) {
            if(Integer.valueOf(type)==0){//个人
                authVo.setIdPhoto(UploadFile(tenantId,multipartfiles[0]));
            }else if(Integer.valueOf(type)==1){//公司认证
                authVo.setType01Prop01(UploadFile(tenantId,multipartfiles[1]));
                authVo.setType02Prop03(UploadFile(tenantId,multipartfiles[2]));
                authVo.setType03Prop02(UploadFile(tenantId,multipartfiles[3]));
                authVo.setType03Prop04(UploadFile(tenantId,multipartfiles[4]));
            }
        }
        RestResponse restResponse =null;
        if(Integer.valueOf(type)==Tenant.AUTH_ONESELF){
            restResponse = savePrivateAuth(request,authVo);
        }else if(Integer.valueOf(type)==Tenant.AUTH_COMPANY){
            restResponse = saveCorpAuth(request,authVo,upgrade);
        }
        ModelAndView mav = new ModelAndView();
        if(restResponse.isSuccess()){
            mav = new ModelAndView("redirect:/console/account/auth/index");
        }else{
            mav.addObject("msg","操作失败，请稍后重试");
            if(StringUtil.isNotEmpty(upgrade)){
                logger.info("个人认证  升级到企业认证:{}",upgrade);
                mav.addObject("upgrade",true);//个人认证  升级到企业认证
            }
            mav.setViewName("/console/account/auth/index");
        }
        return mav;
    }

    /**
     * 保存个人实名认证rest请求
     * @param authVo 实名认证ao
     * @return
     */
    private RestResponse savePrivateAuth(HttpServletRequest request,AuthVo authVo){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +  "/rest/account/auth/save_private_auth";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(authVo, Map.class);
        map.put("status",Tenant.AUTH_ONESELF_WAIT);//状态
        map.put("name",authVo.getPrivateName());//姓名
        return  RestRequest.buildSecurityRequest(token).post(uri,map, RealnamePrivate.class);
    }

    /**
     * 保存企业实名认证rest请求
     * @param authVo 实名认证ao
     * @return
     */
    private RestResponse saveCorpAuth(HttpServletRequest request, AuthVo authVo,String upgrade){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/account/auth/save_corp_auth";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(authVo, Map.class);
        if(StringUtil.isNotEmpty(upgrade)){
            logger.info("个人认证  升级到企业认证:{}",upgrade);
            map.put("status",Tenant.AUTH_UPGRADE_WAIT);//状态
        }else{
            map.put("status",Tenant.AUTH_WAIT);//状态
        }
        map.put("name",authVo.getCorpName());//企业名称
        return  RestRequest.buildSecurityRequest(token).post(uri,map, RealnameCorp.class);
    }

}
