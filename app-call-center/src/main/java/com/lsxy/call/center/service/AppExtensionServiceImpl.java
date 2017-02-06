package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.opensips.service.OpensipsExtensionService;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.dao.AppExtensionDao;
import com.lsxy.call.center.api.states.lock.ExtensionLock;
import com.lsxy.call.center.api.states.state.ExtensionState;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zhangxb on 2016/10/21.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class AppExtensionServiceImpl extends AbstractService<AppExtension> implements AppExtensionService {

    private static final Logger logger = LoggerFactory.getLogger(AppExtensionServiceImpl.class);

    @Autowired
    AppExtensionDao appExtensionDao;

    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    ExtensionState extensionState;
    @Autowired
    AppService appService;
    @Autowired
    private OpensipsExtensionService opensipsExtensionService;

    @Override
    public BaseDaoInterface<AppExtension, Serializable> getDao() {
        return this.appExtensionDao;
    }

    private boolean exists(String user){
        if(StringUtil.isEmpty(user)){
            throw new NullPointerException();
        }
        return appExtensionDao.countByUser(user) > 0;
    }

    //创建
    @Override
    public AppExtension create(String appId,AppExtension appExtension) throws YunhuniApiException {
        App app = appService.findById(appId);

        appExtension.setAppId(appId);
        appExtension.setTenantId(app.getTenant().getId());

        if(appExtension == null || StringUtil.isBlank(appExtension.getTenantId()) || StringUtil.isBlank(appExtension.getAppId())){
            throw new RequestIllegalArgumentException();
        }

        switch (appExtension.getType()){
            case AppExtension.TYPE_SIP:
                appExtension.setTelnum(null);
                appExtension.setIpaddr(null);
                if(StringUtil.isBlank(appExtension.getUser()) || StringUtil.isBlank(appExtension.getPassword())){
                    throw new RequestIllegalArgumentException();
                }
                //只能是纯数字
                String reg = "^\\d{6,12}$";
                boolean uB = Pattern.compile(reg).matcher(appExtension.getUser()).find();
                boolean pB = Pattern.compile(reg).matcher(appExtension.getPassword()).find();
                if(!uB || !pB){
                    throw new RequestIllegalArgumentException();
                }

                Long ccn = app.getCallCenterNum();
                //创建分机的用户名要加上应用编号，以区分唯一性
                appExtension.setUser(ccn + appExtension.getUser());
                //用户名是否已存在
                if(this.exists(appExtension.getUser())){
                    throw new ExtensionUserExistException();
                }
                break;
            case AppExtension.TYPE_THIRD_SIP:
                appExtension.setUser(null);
                appExtension.setPassword(null);
                appExtension.setTelnum(null);
                if(StringUtils.isBlank(appExtension.getIpaddr())){
                    throw new RequestIllegalArgumentException();
                }
            case AppExtension.TYPE_TELPHONE:
                appExtension.setUser(null);
                appExtension.setPassword(null);
                appExtension.setIpaddr(null);
                if(StringUtil.isBlank(appExtension.getTelnum()) ){
                    throw new RequestIllegalArgumentException();
                }
                break;
            default:
                throw new RequestIllegalArgumentException();
        }

        this.save(appExtension);
        if(AppExtension.TYPE_SIP.equals(appExtension.getType())){
            //TODO 分机opensips注册
            opensipsExtensionService.createExtension(appExtension.getUser(),appExtension.getPassword());
        }
        if(AppExtension.TYPE_TELPHONE.equals(appExtension.getType())){
            extensionState.setEnable(appExtension.getId(),ExtensionState.Model.ENABLE_TRUE);
        }
        return appExtension;
    }


    @Override
    public List<AppExtension> findByAppId(String appId) {
        return appExtensionDao.findByAppId(appId);
    }

    @Override
    public void delete(String extensionId, String appId) throws YunhuniApiException{
        AppExtension extension = this.findById(extensionId);
        if(extension == null){
            throw new ExtensionNotExistException();
        }
        if(StringUtils.isNotBlank(appId) && appId.equals(extension.getAppId())){
            //获取分机锁
            ExtensionLock extensionLock = new ExtensionLock(redisCacheService,extensionId);
            boolean lock = extensionLock.lock();
            //获取锁失败 抛异常
            if(!lock){
                throw new ExtensionBindingToAgentException();
            }
            try{
                String agent = extensionState.getAgent(extensionId);
                if(StringUtils.isBlank(agent)){
                    try {
                        this.delete(extension);
                    } catch (Exception e) {
                        logger.error("删除分机失败:{}",extensionId);
                        logger.error("删除分机失败",e);
                        throw new RequestIllegalArgumentException();
                    }
                    if(AppExtension.TYPE_SIP.equals(extension.getType())){
                        //TODO 分机opensips删除
                        opensipsExtensionService.deleteExtension(extension.getUser());
                    }
                    extensionState.delete(extensionId);
                }else{
                    throw new ExtensionBindingToAgentException();
                }
            }finally {
                extensionLock.unlock();
            }
        }else{
            throw new RequestIllegalArgumentException();
        }
    }

    @Override
    public Page<AppExtension> getPage(String appId, Integer pageNo, Integer pageSize) {
        String hql = "from AppExtension obj where obj.appId=?1";
        Page page = this.pageList(hql, pageNo, pageSize, appId);
        List<AppExtension> result = page.getResult();
        if(result != null && result.size() > 0){
            for(AppExtension ext : result){
                boolean enable = extensionState.getEnable(ext.getId());
                ext.setEnable(enable);
            }
        }
        return page;
    }

    @Override
    public AppExtension findOne(String appId, String extensionId) throws YunhuniApiException{
        if(StringUtils.isBlank(appId)){
            logger.error("appId 不能为空");
            throw new RequestIllegalArgumentException();
        }
        if(StringUtils.isBlank(extensionId)){
            logger.error("extension 不能为空");
            throw new RequestIllegalArgumentException();
        }
        AppExtension extension = this.findById(extensionId);
        if(extension == null){
            throw new ExtensionNotExistException();
        }
        if(!appId.equals(extension.getAppId())){
            throw new RequestIllegalArgumentException();
        }
        return extension;
    }

    @Override
    public void login(String user) {
        AppExtension appExtension = appExtensionDao.findByUser(user);
//        Integer expire = 10 * 60 * 1000;
//        ExtensionState.Model model = extensionState.new Model();
//        model.setLastRegisterTime(System.currentTimeMillis());
//        model.setRegisterExpires(expire);
//        model.setEnable(ExtensionState.Model.ENABLE_TRUE);
//        extensionState.setAll(appExtension.getId(),model);
        if(appExtension != null){
            extensionState.setEnable(appExtension.getId(),ExtensionState.Model.ENABLE_TRUE);
        }
    }

    @Override
    public void logout(String user) {
        AppExtension appExtension = appExtensionDao.findByUser(user);
        if(appExtension != null){
            extensionState.setEnable(appExtension.getId(),ExtensionState.Model.ENABLE_FALSE);
        }
    }



    @Override
    public List<Map<String,Object>> exs(String appId){
        List<Map<String,Object>> exs = new ArrayList<>();
        List<AppExtension> exs1 = this.findByAppId(appId);
        if(exs1 != null && exs1.size()>0){
            for (AppExtension ex : exs1){
                Map<String,Object> map = new HashMap<>();
                map.put(ex.getId(),extensionState.getEnable(ex.getId()));
                exs.add(map);
            }
        }
        return exs;
    }
}
