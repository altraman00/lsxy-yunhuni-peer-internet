package com.lsxy.app.portal.rest.file;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.billing.model.Billing;
import com.lsxy.yunhuni.api.billing.service.BillingService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 * 放音文件
 * Created by zhangxb on 2016/7/21.
 */
@RequestMapping("/rest/voice_file_play")
@RestController
public class VoiceFilePlayController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayController.class);
    @Autowired
    VoiceFilePlayService voiceFilePlayService;
    @Autowired
    private OSSService ossService;
    @Autowired
    private BillingService billingService;
    /**
     * 根据放音文件id删除放音文件
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public RestResponse delete(String id) throws InvocationTargetException, IllegalAccessException {
        String repository=SystemConfig.getProperty("global.oss.aliyun.bucket");
        VoiceFilePlay voiceFilePlay =  voiceFilePlayService.findById(id);
        try {
            ossService.deleteObject(repository, voiceFilePlay.getFileKey());
            //删除OSS文件成功，删除数据库记录
            voiceFilePlayService.delete(voiceFilePlay);
            //删除记录成功,更新剩余存储容量大小
            Billing billing = billingService.findBillingByUserName(getCurrentAccountUserName());
            billing.setFileRemainSize(billing.getFileRemainSize()+voiceFilePlay.getSize());
        }catch(Exception e){
            logger.error("删除OSS文件：{1}失败，异常{2}",voiceFilePlay.getFileKey(),e);
            return RestResponse.failed("0000","OSS删除失败");
        }
        return RestResponse.success(voiceFilePlay);
    }

    /**
     * 新建放音文件记录
     * @param voiceFilePlay
     * @return
     */
    @RequestMapping("/create")
    public RestResponse createRemark(VoiceFilePlay voiceFilePlay){
        //将对象保存数据库
        voiceFilePlay = voiceFilePlayService.save(voiceFilePlay);
        //更新账户表
        Billing billing = billingService.findBillingByUserName(getCurrentAccountUserName());
        billing.setFileRemainSize(billing.getFileRemainSize()-voiceFilePlay.getSize());
        return RestResponse.success(voiceFilePlay);
    }
    /**
     * 根据id修改备注
     * @param id
     * @param remark 新备注
     * @return
     */
    @RequestMapping("/modify")
    public RestResponse modifyRemark(String id,String remark){
        VoiceFilePlay voiceFilePlay = voiceFilePlayService.findById(id);
        voiceFilePlay.setRemark(remark);
        voiceFilePlay = voiceFilePlayService.save(voiceFilePlay);
        return RestResponse.success(voiceFilePlay);
    }

    /**
     * 根据名字和应用id查询用户名下的放音文件
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param name 名字
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/plist")
    public RestResponse pageList(Integer pageNo,Integer pageSize,String name,String appId){
        Tenant tenant = getCurrentAccount().getTenant();
        Page<VoiceFilePlay> page = voiceFilePlayService.pageList(pageNo,pageSize,name,tenant.getId(),appId);
        return RestResponse.success(page);
    }
}
