package com.lsxy.app.portal.rest.app;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.portal.VoiceFilePlayDeleteEvent;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.GlobalConfig;
import com.lsxy.yunhuni.api.config.model.TenantConfig;
import com.lsxy.yunhuni.api.config.service.GlobalConfigService;
import com.lsxy.yunhuni.api.config.service.TenantConfigService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 应用RestApp
 * Created by liups on 2016/6/29.
 */
@RequestMapping("/rest/app")
@RestController
public class AppController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private AppService appService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private AppOnlineActionService appOnlineActionService;
    @Autowired
    private VoiceFilePlayService voiceFilePlayService;
    @Autowired
    private OSSService ossService;
    @Autowired
    private MQService mqService;
    @Autowired
    private TenantConfigService tenantConfigService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private ResourcesRentService resourcesRentService;
    @Autowired
    VoiceFileRecordService voiceFileRecordService;
    @RequestMapping("/get/{id}/recording/time")
    public RestResponse getRecordingTimeByTenantIdAndAppId(@PathVariable String id){
        int re = tenantConfigService.getRecordingTimeByTenantIdAndAppId(getCurrentAccount().getTenant().getId(),id);
        return RestResponse.success(re);
    }
    @RequestMapping("/edit/{id}/recording/{time}")
    public RestResponse getRecordingTimeByTenantIdAndAppId(@PathVariable String id,@PathVariable int time){
        Pattern pattern = Pattern.compile("^[0-9]*[1-9][0-9]*$");
        int globalRecording = 7;//
        GlobalConfig globalConfig = globalConfigService.findByTypeAndName(GlobalConfig.TYPE_RECORDING,GlobalConfig.KEY_RECORDING);
        if(globalConfig!=null&& StringUtils.isNotEmpty(globalConfig.getValue())) {
            //全局录音文件存储时长时间
            Matcher matcher = pattern.matcher(globalConfig.getValue());
            if (matcher.matches()) {
                globalRecording = Integer.valueOf(globalConfig.getValue());
            }
        }
        int tenantRecording = 0;
        TenantConfig tenantConfig = tenantConfigService.findByTypeAndKeyNameAndTenantIdAndAppId(GlobalConfig.TYPE_RECORDING, GlobalConfig.KEY_RECORDING, getCurrentAccount().getTenant().getId(), id);
        if(tenantConfig!=null) {
            Matcher matcher1 = pattern.matcher(tenantConfig.getValue());
            if (matcher1.matches()) {
                tenantRecording = Integer.valueOf(tenantConfig.getValue());
            }
            if(tenantRecording<=globalRecording&&time>globalRecording){//收费
                boolean flag = voiceFileRecordService.recordCost(getCurrentAccount().getTenant().getId(),id);
                if(!flag){
                    return RestResponse.failed("-1","余额不足");
                }
            }
            tenantConfig.setValue(time+"");
            tenantConfigService.save(tenantConfig);
        }else{
            if(time>globalRecording){//收费
                boolean flag = voiceFileRecordService.recordCost(getCurrentAccount().getTenant().getId(),id);
                if(!flag){
                    return RestResponse.failed("-1","余额不足");
                }
            }
            tenantConfig = new TenantConfig( getCurrentAccount().getTenant().getId(),  id, GlobalConfig.TYPE_RECORDING, GlobalConfig.KEY_RECORDING, "录音文件存储时长", time+"", "on");
            tenantConfigService.save(tenantConfig);
        }
        return RestResponse.success("修改成功");
    }
    /**
     * 根据应用名字查找应用数
     * @param name 应用名字
     * @return
     */
    @RequestMapping("/count/{name}")
    public RestResponse countByTenantIdAndName(@PathVariable String name){
        long re = appService.countByTenantIdAndName(getCurrentAccount().getTenant().getId(),name);
        return RestResponse.success(re);
    }

    /**
     * 查找当前用户的应用
     * @throws Exception
     */
    @RequestMapping("/list")
    public RestResponse listApp(String serviceType) throws Exception{
        List<App> apps = null;
        if(StringUtils.isNotEmpty(serviceType)) {
            apps = appService.findAppByTenantIdAndServiceType(getCurrentAccount().getTenant().getId(),serviceType);
        }else{
            apps = appService.findAppByUserName(getCurrentAccount().getTenant().getId());
        }
        return RestResponse.success(apps);
    }

    /**
     * 查找用户下的分页信息
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping("/plist")
    public RestResponse pageList(Integer pageNo,Integer pageSize){
        Page<App> page = appService.pageList(getCurrentAccount().getTenant().getId(),pageNo,pageSize);
        return RestResponse.success(page);
    }

    /**
     * 根据appId查找应用
     * @param id 应用id
     * @return
     */
    @RequestMapping("/get/{id}")
    public RestResponse findById(@PathVariable String id){
        App app = appService.findById(id);
        return RestResponse.success(app);
    }
    /**
     * 删除应用
     * @param id
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/delete")
    public RestResponse delete(String id) throws InvocationTargetException, IllegalAccessException {
        boolean flag = appService.isAppBelongToUser(getCurrentAccountUserName(), id);
        if(flag){
            App resultApp = appService.findById(id);
            appService.deleteApp(resultApp.getId());
            deletedVF(resultApp);
            return RestResponse.success();
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }

    /**
     * 删除应用下放音文件
     */
    private void deletedVF(App app){
        try {
            //查询应用下的放音文件
            List<VoiceFilePlay> list = voiceFilePlayService.findByAppId(app.getId());
            if(list.size()>0) {
                //更新文件状态为删除
                voiceFilePlayService.updateDeletedByAppId(app.getId());
                //删除oss上的应用放音文件
                List<String> ossDLsit = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    ossDLsit.add(list.get(i).getFileKey());
                }
                String repository = SystemConfig.getProperty("global.oss.aliyun.bucket");
                List<String> reList = ossService.deleteObjects(repository, ossDLsit);
                //批量OSS删除成功的更新状态
                if(reList!=null&&reList.size()>0) {
                    List<String> ossDeletedOk = new ArrayList();
                    for(int i=0;i<list.size();i++){
                        if(reList.contains(list.get(i).getFileKey())){
                            ossDeletedOk.add(list.get(i).getId());
                        }
                    }
                    voiceFilePlayService.batchUpdateValueByKey(ossDeletedOk, "oss_deleted", 1);
                }
                //发起删除区域代理上的文件的事件
                mqService.publish(new VoiceFilePlayDeleteEvent(VoiceFilePlayDeleteEvent.APP,app.getTenant().getId(),app.getId()));
            }
        }catch (Exception e){
            logger.error("删除应用下放音文件,操作失败{}",e);
        }
    }
    /**
     * 新建应用
     * @param app app对象
     * @return
     */
    @RequestMapping("/create")
    public RestResponse create(App app ){
        String userName = getCurrentAccountUserName();
        Tenant tenant = tenantService.findTenantByUserName(userName);
        app.setTenant(tenant);
        appService.create(app);
        return RestResponse.success(app);
    }
    /**
     * 新建应用
     * @param app app对象
     * @return
     */
    @RequestMapping("/update")
    public RestResponse update(App app ) throws InvocationTargetException, IllegalAccessException {
        String userName = getCurrentAccountUserName();
        boolean flag = appService.isAppBelongToUser(userName, app.getId());
        if(flag){
            App resultApp = appService.findById(app.getId());
            if(!resultApp.getName().equals(app.getName())){
                long re = appService.countByTenantIdAndName(getCurrentAccount().getTenant().getId(),app.getName());
                if(re>0){
                    return RestResponse.failed("0000","该应用名已经存在");
                }
            }
            EntityUtils.copyProperties(resultApp,app);
            resultApp = appService.save(resultApp);
            //应用修改后，将其上线步骤清空
            appOnlineActionService.resetAppOnlineAction(app.getId());
            return RestResponse.success(resultApp);
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }
}
