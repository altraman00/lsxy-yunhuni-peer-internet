package com.lsxy.app.portal.rest.file;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.mq.events.portal.VoiceFilePlayDeleteEvent;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.framework.api.billing.service.BillingService;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * 放音文件
 * Created by zhangxb on 2016/7/21.
 */
@RequestMapping("/rest/voice_file_play")
@RestController
public class VoiceFilePlayController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayController.class);
    @Autowired
    private VoiceFilePlayService voiceFilePlayService;
    @Autowired
    private AppService appService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private OSSService ossService;
    @Autowired
    private CalBillingService calBillingService;
    @Autowired
    private MQService mqService;
    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;
    /**
     * 根据放音文件id删除放音文件
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public RestResponse delete(String id) throws InvocationTargetException, IllegalAccessException {
        VoiceFilePlay voiceFilePlay = deleteOneVoiceFilePlay(id);
        return RestResponse.success(voiceFilePlay);
    }

    private VoiceFilePlay deleteOneVoiceFilePlay(String id) throws IllegalAccessException, InvocationTargetException {
        VoiceFilePlay voiceFilePlay =  voiceFilePlayService.findById(id);
        try {
            String repository= SystemConfig.getProperty("global.oss.aliyun.bucket");
            ossService.deleteObject(repository, voiceFilePlay.getFileKey());
            voiceFilePlay.setOssDeleted(VoiceFilePlay.DELETED_SUCCESS);
        }catch(Exception e){
            logger.error("删除OSS文件：{1}失败，异常{2}",voiceFilePlay.getFileKey(),e);
            voiceFilePlay.setOssDeleted(VoiceFilePlay.DELETED_FAIL);
        }
        try{
            mqService.publish(new VoiceFilePlayDeleteEvent(VoiceFilePlayDeleteEvent.FILE,voiceFilePlay.getTenant().getId(),
                    voiceFilePlay.getApp().getId(),voiceFilePlay.getId(),voiceFilePlay.getName()));
        }catch (Exception e){
            logger.error("删除区域文件：{1}失败，异常{2}",voiceFilePlay.getFileKey(),e);
            voiceFilePlay.setAaDeleted(VoiceFilePlay.DELETED_FAIL);
        }
        //删除OSS文件成功，删除数据库记录
        voiceFilePlayService.delete(voiceFilePlay);
        //删除记录成功,更新剩余存储容量大小
//            Billing billing = billingService.findBillingByUserName(getCurrentAccountUserName());
//            billing.setFileRemainSize(billing.getFileRemainSize()+voiceFilePlay.getSize());
//            billingService.save(billing);
        Account account = getCurrentAccount();
        calBillingService.incAddFsize(account.getTenant().getId(),new Date(),voiceFilePlay.getSize());
        return voiceFilePlay;
    }

    /**
     * 查看改文件名的数量
     * @param appId 应用id
     * @param name 文件名
     * @return
     */
    @RequestMapping("/count/name")
    public RestResponse countName(String appId,String name,String subId){
        if(StringUtils.isNotEmpty(subId)){
            ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findByCerbId( subId );
            if(apiCertificateSubAccount == null){
                return RestResponse.failed("","子账号不存在");
            }else{
                subId = apiCertificateSubAccount.getId();
            }
        }else{
            subId = null;
        }
        List list = voiceFilePlayService.findByFileName(getCurrentAccount().getTenant().getId(),appId,name,subId);
        return RestResponse.success(list.size());
    }
    /**
     * 新建放音文件记录
     * @param voiceFilePlay
     * @return
     */
    @RequestMapping("/create")
    public RestResponse createVoiceFilePlay(VoiceFilePlay voiceFilePlay,String appId,String subId) throws InvocationTargetException, IllegalAccessException {
        //将对象保存数据库
        if(logger.isDebugEnabled()) {
            logger.debug("开始创建放音文件记录，应用{}，记录{}", appId, voiceFilePlay);
        }
        App app = appService.findById(appId);
        Account account = getCurrentAccount();
        if(logger.isDebugEnabled()) {
            logger.debug("判断应用{}是否有重名文件{}", appId, voiceFilePlay.getName());
        }
        if(StringUtils.isNotEmpty(subId)){
            ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findByCerbId( subId );
            if(apiCertificateSubAccount == null){
                return RestResponse.failed("","子账号不存在");
            }else{
                subId = apiCertificateSubAccount.getId();
            }
        }else{
            subId = null;
        }
        List<VoiceFilePlay> list = voiceFilePlayService.findByFileName(getCurrentAccount().getTenant().getId(),appId,voiceFilePlay.getName(),subId);
        if(list!=null) {
            for (int i = 0; i < list.size(); i++) {
                if(logger.isDebugEnabled()) {
                    logger.debug("开始删除重名文件对象{}",list.get(i));
                }
                deleteOneVoiceFilePlay(list.get(i).getId());
                if(logger.isDebugEnabled()) {
                    logger.debug("删除重名文件对象{},文件名{}，删除成功",list.get(i).getId(),list.get(i).getName());
                }
            }
        }else{
            if(logger.isDebugEnabled()) {
                logger.debug("没有重名文件");
            }
        }
        voiceFilePlay.setRemark("");
        voiceFilePlay.setApp(app);
        voiceFilePlay.setTenant(account.getTenant());
        voiceFilePlay.setStatus(VoiceFilePlay.STATUS_WAIT);
        voiceFilePlay.setSubaccountId(subId);
        voiceFilePlay = voiceFilePlayService.save(voiceFilePlay);
                //更新账户表
//        Billing billing = billingService.findBillingByUserName(getCurrentAccountUserName());
//        billing.setFileRemainSize(billing.getFileRemainSize()-voiceFilePlay.getSize());
//        billingService.save(billing);
        calBillingService.incUseFsize(account.getTenant().getId(),new Date(),voiceFilePlay.getSize());
        if(logger.isDebugEnabled()) {
            logger.debug("创建放音文件记录成功，应用{}，记录{}", appId, voiceFilePlay);
        }
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
    public RestResponse pageList(Integer pageNo,Integer pageSize,String name,String appId,String subId){
        Tenant tenant = getCurrentAccount().getTenant();
        Page<VoiceFilePlay> page = voiceFilePlayService.pageList(pageNo,pageSize,name,appId,new String[]{tenant.getId()},null,null,null,subId);
        return RestResponse.success(page);
    }
}
