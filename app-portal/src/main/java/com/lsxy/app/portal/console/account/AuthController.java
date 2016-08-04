package com.lsxy.app.portal.console.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.api.tenant.model.RealnameCorp;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.config.SystemConfig;
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

    private  static final Integer AUTH_WAIT = 0;//个人认证等待中
    private  static final Integer  AUTH_NO= 100;//未认证
    private  static final Integer AUTH_COMPANY_FAIL = -2;//企业认证失败
    private  static final Integer AUTH_COMPANY_SUCESS = 2;//企业认证成功
    private  static final Integer AUTH_ONESELF_FAIL = -1;//个人认证失败
    private  static final Integer AUTH_ONESELF_SUCESS = 1;//个人认证成功
    private static final String UPLOAD_TYPE_FILE = "file";//文件上传类型之file
    private static final String UPLOAD_TYPE_OSS = "oss";//文件上传类型之oss
    private static final Integer AUTH_COMPANY=1;//认证类型-企业认证
    private static final Integer AUTH_ONESELF=0;//认证类型-个人认证
    private static final String IS_FALSE = "-1";//表示失败
    private static final String IS_TRUE = "1";//表示成功
    private static final String IS_AUTH = "0";//表示已实名认证
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");
    /**
     * 实名认证首页
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        //TODO 获取实名认证的状态
        String userName = "user001";
        //调resr接口

        RestResponse<HashMap> restResponse = findAuthStatus(request);
        HashMap hs = restResponse.getData();
        if(hs==null){//未实名认证
            // 未实名认证
            mav.setViewName("/console/account/auth/index");
        }else {
            int authStatus  = Integer.valueOf((hs.get("status")+""));
            if(AUTH_NO==authStatus) {
                mav.setViewName("/console/account/auth/index");
            }else if (AUTH_WAIT ==authStatus ) {
                //审核中
                mav.setViewName("/console/account/auth/wait");
            } else if (AUTH_ONESELF_SUCESS == authStatus) {
                // 个人实名认证
                mav.addAllObjects(hs);
                mav.setViewName("/console/account/auth/sucess");
            } else if (AUTH_COMPANY_SUCESS == authStatus) {
                // 企业实名认证
                mav.addAllObjects(hs);
                mav.setViewName("/console/account/auth/sucess");
            } else if (AUTH_ONESELF_FAIL == authStatus) {
                //TODO 个人实名认证失败
                mav.addObject("msg","身份证与名称不符合，请重新提交资料认证");
                mav.setViewName("/console/account/auth/fail");
            } else if (AUTH_COMPANY_FAIL == authStatus) {
                //TODO 企业实名认证失败
                mav.addObject("msg","上传资料不符合要求，请重新提交资料认证");
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
     * 获取后台状态的rest请求方法
     * @return
     */
    private RestResponse findAuthStatus(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl + "/rest/account/auth/find_auth_status";
        Map map = new HashMap();
        return  RestRequest.buildSecurityRequest(token).post(uri,map, HashMap.class);
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
    public ModelAndView edit(HttpServletRequest request,AuthVo authVo,String type, @RequestParam("file") MultipartFile[] multipartfiles) throws IOException{

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

        String  status = IS_TRUE;//默认操作成功
        if(Integer.valueOf(type)==AUTH_ONESELF){
            RestResponse<RealnamePrivate> restResponse = savePrivateAuth(request,authVo);
            RealnamePrivate realnamePrivate = restResponse.getData();
            if (realnamePrivate == null) {
                status = IS_FALSE;
            }

        }else if(Integer.valueOf(type)==AUTH_COMPANY){
            RestResponse<RealnameCorp> restResponse = saveCorpAuth(request,authVo);
            RealnameCorp realnameCorp = restResponse.getData();
            if (realnameCorp == null) {
                status = IS_FALSE;
            }
        }
        ModelAndView mav = new ModelAndView();
        if(IS_TRUE.equals(status)){
            mav = new ModelAndView("redirect:/console/account/auth/index");
        }else{
            mav.addObject("msg","操作失败，请稍后重试");
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
        map.put("status",AUTH_WAIT);//状态
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
        map.put("status",AUTH_WAIT);//状态
        map.put("name",authVo.getCorpName());//企业名称
        return  RestRequest.buildSecurityRequest(token).post(uri,map, RealnameCorp.class);
    }

}
