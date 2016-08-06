package com.lsxy.app.portal.console.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.app.portal.base.AbstractPortalController;
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
    private static final Integer AUTH_COMPANY=1;//认证类型-企业认证
    private static final Integer AUTH_ONESELF=0;//认证类型-个人认证

    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");
    /**
     * 实名认证首页
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(@RequestParam(value = "upgrade",required = false) String upgrade,HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        //TODO 获取实名认证的状态
        //调resr接口
        RestResponse<HashMap> restResponse = findAuthInfo(request);
        HashMap hs = restResponse.getData();
        if(hs==null){//未实名认证
            logger.info("未实名认证");
            // 未实名认证
            mav.setViewName("/console/account/auth/index");
        }else {
            int authStatus  = Integer.valueOf((hs.get("status")+""));
            if(Tenant.AUTH_NO==authStatus) {//未认证
                logger.info("未实名认证:{}",authStatus);
                mav.setViewName("/console/account/auth/index");
            }else if (Tenant.AUTH_WAIT ==authStatus ) {//审核中
                //审核中
                mav.setViewName("/console/account/auth/wait");
            } else if (Tenant.AUTH_COMPANY_SUCCESS == authStatus) {
                // 企业实名认证
                mav.addAllObjects(hs);
                mav.setViewName("/console/account/auth/sucess");
            } else if (Tenant.AUTH_ONESELF_SUCCESS == authStatus) {
                if(StringUtil.isNotEmpty(upgrade)){
                    logger.info("个人认证  升级到企业认证:{}",upgrade);
                    mav.addObject("upgrade",true);//个人认证  升级到企业认证
                    mav.setViewName("/console/account/auth/index");
                }else{
                    // 个人实名认证
                    mav.addAllObjects(hs);
                    mav.setViewName("/console/account/auth/sucess");
                }
            } else if (Tenant.AUTH_COMPANY_FAIL == authStatus) {
                //TODO 企业实名认证失败
                mav.addObject("msg","上传资料不符合要求，请重新提交资料认证");
                mav.setViewName("/console/account/auth/fail");
            } else if (Tenant.AUTH_ONESELF_FAIL == authStatus) {
                //TODO 个人实名认证失败
                mav.addObject("msg","身份证与名称不符合，请重新提交资料认证");
                mav.setViewName("/console/account/auth/fail");
            }else{
                // 未实名认证
                mav.setViewName("/console/account/auth/index");
            }
        }
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
     * 获取实名认证信息的rest请求方法
     * @return
     */
    private RestResponse findAuthInfo(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl + "/rest/account/auth/find_auth_info";
        return  RestRequest.buildSecurityRequest(token).get(uri,HashMap.class);
    }
    /**
     * 获取后台状态的rest请求方法
     * @return
     */
    private RestResponse findAuthStatus(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl + "/rest/account/auth/find_auth_status";
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
        if(Integer.valueOf(type)==AUTH_ONESELF){
            restResponse = savePrivateAuth(request,authVo);
        }else if(Integer.valueOf(type)==AUTH_COMPANY){
            restResponse = saveCorpAuth(request,authVo);
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
        String uri = restPrefixUrl +  "/rest/account/auth/save_private_auth";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(authVo, Map.class);
        map.put("status",Tenant.AUTH_WAIT);//状态
        map.put("name",authVo.getPrivateName());//姓名
        return  RestRequest.buildSecurityRequest(token).post(uri,map, RealnamePrivate.class);
    }

    /**
     * 保存企业实名认证rest请求
     * @param authVo 实名认证ao
     * @return
     */
    private RestResponse saveCorpAuth(HttpServletRequest request, AuthVo authVo){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl + "/rest/account/auth/save_corp_auth";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(authVo, Map.class);
        map.put("status",Tenant.AUTH_WAIT);//状态
        map.put("name",authVo.getCorpName());//企业名称
        return  RestRequest.buildSecurityRequest(token).post(uri,map, RealnameCorp.class);
    }

}
