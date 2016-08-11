package com.lsxy.app.oc.rest.account;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.RealnamePrivateService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实名认证处理类
 * Created by zhangxb on 2016/8/11.
 */
@RequestMapping("/demand")
@RestController
public class AuthController extends AbstractRestController {
    @Autowired
    RealnamePrivateService realnamePrivateService;
    /**
     * 查找用户下的分页信息
     * @param authStatus await|auditing|unauth
     * @param startTime 开始时间 yyyy-MM-dd
     * @param endTime 结束时间
     * @param type 0个人认证 1企业认证
     * @param search 会员名
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping(value = "/member/{authStatus}/list",method = RequestMethod.GET)
    public RestResponse pageList(@PathVariable String authStatus, String startTime, String endTime, Integer type, String search, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        Integer status = null;
        if("await".equals(authStatus)){
            status = 0;
        }else if("auditing".equals(authStatus)){
            status = 1;
        }else if("unauth".equals(authStatus)){
            status = -1;
        }
        Page page = null;
        if(status!=null) {
            page = realnamePrivateService.pageListAuthInfo(status,startTime,endTime,type,search,pageNo,pageSize);
        }
        return RestResponse.success(page);
    }
}
