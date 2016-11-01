package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.dao.AppExtensionDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class AppExtensionServiceImpl extends AbstractService<AppExtension> implements AppExtensionService {

    private static final Logger logger = LoggerFactory.getLogger(AppExtensionServiceImpl.class);

    @Autowired
    AppExtensionDao appExtensionDao;

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
    public boolean register(AppExtension appExtension){
        if(appExtension == null){
            throw new NullPointerException();
        }
        if(StringUtil.isEmpty(appExtension.getTenantId())){
            throw new NullPointerException();
        }
        if(StringUtil.isEmpty(appExtension.getAppId())){
            throw new NullPointerException();
        }
        if(StringUtil.isEmpty(appExtension.getUser())){
            throw new NullPointerException();
        }
        if(StringUtil.isEmpty(appExtension.getPassword())){
            throw new NullPointerException();
        }
        if(this.exists(appExtension.getUser())){
            logger.info("已存在的分机账号{}",appExtension);
            return false;
        }
        this.save(appExtension);
        return true;
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
    public boolean updateRegisterStatus(String id,Integer lastRegisterStatus){
        if(StringUtil.isEmpty(id)){
            return false;
        }
        AppExtension appExtension = this.findById(id);
        if(appExtension == null){
            return false;
        }
        appExtension.setLastRegisterStatus(lastRegisterStatus);
        appExtension.setLastRegisterTime(new Date());
        try{
            this.save(appExtension);
            return true;
        }catch (Throwable t){
            logger.error("",t);
        }
        return false;
    }

    @Override
    public boolean updateLastAction(String id,Integer lastAction){
        if(StringUtil.isEmpty(id)){
            return false;
        }
        AppExtension appExtension = this.findById(id);
        if(appExtension == null){
            return false;
        }
        appExtension.setLastAction(lastAction);
        appExtension.setLastActionTime(new Date());
        try{
            this.save(appExtension);
            return true;
        }catch (Throwable t){
            logger.error("",t);
        }
        return false;
    }

    @Override
    public boolean updateAgent(String id,String agent,Integer enable){
        if(StringUtil.isEmpty(id)){
            return false;
        }
        AppExtension appExtension = this.findById(id);
        if(appExtension == null){
            return false;
        }
        appExtension.setAgent(agent);
        appExtension.setEnabled(enable==null?0:enable);
        try{
            this.save(appExtension);
            return true;
        }catch (Throwable t){
            logger.error("",t);
        }
        return false;
    }

    @Override
    public List<AppExtension> findByAppId(String appId) {
        return appExtensionDao.findByAppId(appId);
    }

}
