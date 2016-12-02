package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.opensips.service.OpensipsService;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.dao.AppExtensionDao;
import com.lsxy.call.center.states.lock.ExtensionLock;
import com.lsxy.call.center.states.state.ExtensionState;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.JSONUtil;
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
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
    @Reference(timeout=3000,check = false,lazy = true)
    private OpensipsService opensipsService;

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

    //注册
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
                String reg = "^\\d*$";
                boolean b = Pattern.compile(reg).matcher(appExtension.getUser()).find();
                if(!b){
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
            opensipsService.createExtension(appExtension.getUser(),appExtension.getPassword());
        }
        //TODO 初始化状态状态
        extensionState.setLastRegisterStatus(appExtension.getId(),200);

        return appExtension;
    }

    //鉴权
    @Override
    public boolean login(String tenantId,String appId,String user,String pass){
        if(StringUtil.isEmpty(tenantId)){
            return false;
        }
        if(StringUtil.isEmpty(appId)){
            return false;
        }
        if(StringUtil.isEmpty(user)){
            return false;
        }
        if(StringUtil.isEmpty(pass)){
            return false;
        }
        AppExtension appExtension = null;
        try{
            appExtension = appExtensionDao.findByTenantIdAndAppIdAndUser(tenantId,appId,user);
        }catch (Throwable t){
            logger.error("",t);
        }
        if(appExtension == null){
            return false;
        }
        if(pass.equals(appExtension.getPassword())){
            return true;
        }
        return false;
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
                        opensipsService.deleteExtension(extension.getUser());
                    }
                    redisCacheService.del(extensionId);
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
        return this.pageList(hql, pageNo, pageSize, appId);
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
        if(!appId.equals(extension.getAppId())){
            throw new RequestIllegalArgumentException();
        }
        return extension;
    }

    @Override
    public void register(String extensionId) {
        Integer expire = 10 * 60 * 1000;
        ExtensionState.Model model = extensionState.new Model();
        model.setLastRegisterStatus(200);
        model.setLastRegisterTime(System.currentTimeMillis());
        model.setRegisterExpires(expire);
        extensionState.setAll(extensionId,model);
    }


}
