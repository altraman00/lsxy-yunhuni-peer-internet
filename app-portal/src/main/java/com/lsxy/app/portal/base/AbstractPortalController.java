package com.lsxy.app.portal.base;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.exceptions.TokenMissingException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.security.SecurityUser;
import com.lsxy.framework.core.utils.ExportExcel;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.app.model.App;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by liups on 2016/6/28.
 */
public abstract class AbstractPortalController {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPortalController.class);
    /**
     * 对Controller层统一的异常处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exp(HttpServletRequest request,HttpServletResponse response,Exception ex) {
        logger.error("异常",ex);
        ModelAndView mav;
        //Ajax请求带有X-Requested-With:XMLHttpRequest
        String xRequestedWith = request.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(xRequestedWith) && "XMLHttpRequest".equals(xRequestedWith)) {
            String result = JSONUtil.objectToJson(RestResponse.failed("0000",ex.getMessage()));
            try {
                response.getWriter().write(result);
            }catch (Exception e){}
            return null;
        }else{
            mav = new ModelAndView("error_page");
        }
        return mav;
    }

    /**
     * 获取当前登录用户的授权token
     * @param request
     * @return
     */
    public String getSecurityToken(HttpServletRequest request){
        String token = null;
        Object obj = request.getSession().getAttribute(PortalConstants.SSO_TOKEN);
        if(logger.isDebugEnabled()){
            logger.debug("get security token is : {}" , obj);
        }
        if(obj != null && obj instanceof String){
            token = (String) obj;
        }
        if(token == null){
            throw new TokenMissingException("token丢失");
        }
        return token;
    }

    /**
     * 获取用户的当前对象
     * @param request
     * @return
     */
    public Account getCurrentAccount(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/account/get/current";
        RestResponse<Account> restResponse = RestRequest.buildSecurityRequest(token).get(uri, Account.class);
        Account account = restResponse.getData();
        return account;
    }
    /**
     * 获取租户下的全部应用
     * @param request
     * @return
     */
    public RestResponse getAppList(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL + "/rest/app/list";
        return RestRequest.buildSecurityRequest(token).getList(uri, App.class);
    }
    public RestResponse getBillAppList(HttpServletRequest request,String serviceType){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL + "/rest/app/list?serviceType={1}";
        return RestRequest.buildSecurityRequest(token).getList(uri, App.class,serviceType);
    }
    public RestResponse getSubAccountList(HttpServletRequest request,String appId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL + "/rest/sub_account/list?appId={1}";
        return RestRequest.buildSecurityRequest(token).getList(uri, ApiCertificateSubAccount.class,appId);
    }
    public Map<String,ApiCertificateSubAccount> getMapSubAccountList(HttpServletRequest request,String appId){
        RestResponse<List<ApiCertificateSubAccount>> restResponse = getSubAccountList(request,appId);
        List<ApiCertificateSubAccount> list =  restResponse.getData();
        Map<String ,ApiCertificateSubAccount> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i).getId(),list.get(i));
        }
        return map;
    }
    /**
     * 获取单个应用
     * @param request
     * @param id
     * @return
     */
    public RestResponse<App> getAppById(HttpServletRequest request,String id ){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL + "/rest/app/get/{1}";
        return RestRequest.buildSecurityRequest(token).get(uri, App.class,id);
    }
    /**
     * 获取当前用户（SecurityUser）
     * @param request
     * @return
     */
    public SecurityUser getCurrentUser(HttpServletRequest request){
        return (SecurityUser) request.getSession().getAttribute("currentUser");
    }

    /**
     * 导出文件
     * @param response
     */
    public <T>  void downloadExcel(String title,String one, String[] headers, String[] values, Collection<T> dataset, String pattern, String money,HttpServletResponse response) {
        try {
            Date d = new Date();
            String name = DateUtils.formatDate(d,"yyyyMMdd")+ d.getTime();
            HSSFWorkbook wb = ExportExcel.exportExcel(title,one,headers,values,dataset,pattern,money);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="+name+".xls");
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 获取实名认证信息的rest请求方法
     * @return
     */
    public RestResponse findAuthInfo(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/account/auth/find_auth_info";
        return  RestRequest.buildSecurityRequest(token).get(uri,HashMap.class);
    }
}
