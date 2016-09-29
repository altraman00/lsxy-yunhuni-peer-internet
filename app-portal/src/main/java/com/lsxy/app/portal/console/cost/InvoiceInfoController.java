package com.lsxy.app.portal.console.cost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.security.AvoidDuplicateSubmission;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.core.security.SecurityUser;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 发票信息
 * Created by liups on 2016/7/14.
 */
@Controller
@RequestMapping("/console/cost/invoice_info")
public class InvoiceInfoController extends AbstractPortalController {
    @Autowired
    private OSSService ossService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView get(HttpServletRequest request){
        String returView;
        Map<String,Object> model = new HashMap<>();
        String token = this.getSecurityToken(request);
        InvoiceInfo invoiceInfo = getInvoiceInfo(token);
        if(invoiceInfo != null){
            returView = "console/cost/invoice/invoice_info";
            model.put("invoiceInfo",invoiceInfo);
        }else{
            returView = "console/cost/invoice/invoice_info_edit";
        }
        return new ModelAndView(returView,model);
    }

    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView edit(HttpServletRequest request){
        String returView;
        Map<String,Object> model = new HashMap<>();
        String token = this.getSecurityToken(request);
        InvoiceInfo invoiceInfo = getInvoiceInfo(token);
        model.put("invoiceInfo",invoiceInfo);
        returView = "console/cost/invoice/invoice_info_edit";
        return new ModelAndView(returView,model);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView edit(HttpServletRequest request, InvoiceInfo invoiceInfo, MultipartFile uploadfile) throws IOException {
        SecurityUser user = this.getCurrentUser(request);
        String imgUrl = "";
        if(uploadfile != null){
            imgUrl = uploadFile(user.getTenantId(), uploadfile);
        }
        if(StringUtils.isNotBlank(imgUrl)){
            invoiceInfo.setQualificationUrl(imgUrl);
        }
        String returView;
        Map<String,Object> model = new HashMap<>();
        String token = this.getSecurityToken(request);
        saveInvoiceInfo(token, invoiceInfo);
        returView = "redirect:/console/cost/invoice_info";
        return new ModelAndView(returView,model);
    }

    private void saveInvoiceInfo(String token, InvoiceInfo invoiceInfo) {
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_info/save";
        Map<String, Object> map = new ObjectMapper().convertValue(invoiceInfo, Map.class);
        RestRequest.buildSecurityRequest(token).post(url, map,InvoiceInfo.class);
    }

    /**
     * 获取用户发票信息
     * @return
     */
    private InvoiceInfo getInvoiceInfo(String token){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_info/get";
        RestResponse<InvoiceInfo> response = RestRequest.buildSecurityRequest(token).get(url, InvoiceInfo.class);
        return response.getData();
    }

    /**
     * 上传文件方法
     */
    private String uploadFile(String tenantId, MultipartFile file) throws IOException {
        return ossService.uploadFile(tenantId,"invoice",file);
    }

}
