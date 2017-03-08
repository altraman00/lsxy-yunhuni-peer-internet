package com.lsxy.app.portal.rest.app;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhangxb on 2017/3/7.
 */
@RequestMapping("/rest/msg_template")
@RestController
public class MsgTemplateController  extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(MsgTemplateController.class);
    @Reference(timeout=3000,check = false,lazy = true)
    private MsgTemplateService msgTemplateService;
    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;
    private String getSubIdsByCerbId(String cerbId){
        StringBuilder stringBuilder = new StringBuilder();
        List<ApiCertificateSubAccount> list = apiCertificateSubAccountService.getListByCerbId(cerbId);
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append("'" + list.get(i).getId() + "'");
            if ((list.size() - 1) != i) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
    @RequestMapping("/plist")
    public RestResponse getPage(@RequestParam(defaultValue = "1") int pageNo,@RequestParam(defaultValue = "20") int pageSize,@RequestParam String appId,String name,String subId ){
        if(StringUtils.isNotEmpty(subId)){
            String subId1 = getSubIdsByCerbId(subId);
            if(StringUtils.isEmpty(subId1)){
                return RestResponse.success(new Page((pageNo-1)*pageSize,pageNo*pageSize,pageSize,null));
            }else{
                subId = subId1;
            }
        }
        Page page = msgTemplateService.getPageByCondition(pageNo,pageSize,appId,subId,name);
        return RestResponse.success(page);
    }
    @RequestMapping(value = "/new")
    public RestResponse create(HttpServletRequest request, String appId, String type, String name,String content, String remark){
        Account account = getCurrentAccount();
        MsgTemplate msgTemplate = new MsgTemplate(account.getTenant().getId(),appId,name,type,content,remark);
        msgTemplateService.createTemplate(msgTemplate);
        return RestResponse.success("成功");
    }
    @RequestMapping(value = "/edit/{id}")
    public RestResponse edit(HttpServletRequest request, @PathVariable String id, String name,String content, String remark){
        Account account = getCurrentAccount();
        MsgTemplate msgTemplate = msgTemplateService.findById(id);
        if(msgTemplate!=null){
            if(msgTemplate.getStatus() == MsgTemplate.STATUS_FAIL){
                msgTemplate.setName( name );
                msgTemplate.setContent( content );
                msgTemplate.setRemark( remark );
                msgTemplate.setStatus(MsgTemplate.STATUS_WAIT);
                msgTemplateService.save(msgTemplate);
                return RestResponse.success("成功");
            }else{
                return RestResponse.failed("","不可修改");
            }
        }else{
            return RestResponse.failed("","记录不存在");
        }
    }
    @RequestMapping(value = "/delete/{id}")
    public RestResponse delete(HttpServletRequest request, @PathVariable String id){
        MsgTemplate msgTemplate = msgTemplateService.findById(id);
        if(msgTemplate!=null){
            try {
                msgTemplateService.delete(msgTemplate);
                return RestResponse.success("成功");
            } catch (Exception e) {
                logger.error("删除记录失败",e);
                return RestResponse.failed("",e.getMessage());
            }
        }else{
            return RestResponse.failed("","记录不存在");
        }
    }
    @RequestMapping(value = "/find/{id}")
    public RestResponse findById(HttpServletRequest request, @PathVariable String id){
        MsgTemplate msgTemplate = msgTemplateService.findById(id);
        if(msgTemplate!=null){
                return RestResponse.success(msgTemplate);
        }else{
            return RestResponse.failed("","记录不存在");
        }
    }
}
