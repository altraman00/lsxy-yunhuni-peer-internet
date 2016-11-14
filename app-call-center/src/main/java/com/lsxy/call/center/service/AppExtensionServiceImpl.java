package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.AppExtensionStatus;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.dao.AppExtensionDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String register(AppExtension appExtension){
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
            throw new RuntimeException("已存在的分机账号");
        }
        this.save(appExtension);
        //TODO 初始化状态状态
        Map map = new HashMap<>();
        AppExtensionStatus extensionStatus = new AppExtensionStatus();
        extensionStatus.setLastRegisterStatus(1);
        try {
            BeanUtils.copyProperties2(map,extensionStatus,false);
        } catch (Exception e) {
        }
        redisCacheService.hputAll(AppExtensionStatus.EXTENSION_STATUS_PREFIX + appExtension.getId(),map);

        return appExtension.getId();
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
    public void delete(String extensionId, String appId) {
        AppExtension extension = this.findById(extensionId);
        if(StringUtils.isNotBlank(appId) && appId.equals(extension.getAppId())){
            try {
                AppExtensionStatus status = new AppExtensionStatus();
                Map map = redisCacheService.hgetAll(AppExtensionStatus.EXTENSION_STATUS_PREFIX + extensionId);
                try {
                    BeanUtils.copyProperties2(status,map,false);
                } catch (Exception e) {
                }
                //TODO 获取分机状态，座席如果没绑定座席则：
                if(StringUtils.isBlank(status.getBindingAgent())){
                    this.delete(extension);
                    redisCacheService.del(extensionId);
                }else{
                    throw new RuntimeException("删除分机失败，分机绑定了座席");
                }
            } catch (Exception e) {
                logger.error("删除分机失败",e);
                //TODO 抛异常
                throw new RuntimeException("删除分机失败");
            }
        }else{
            //TODO 抛异常
            throw new RuntimeException("删除分机失败,参数不正确");
        }
    }

    @Override
    public Page<AppExtension> getPage(String appId, Integer pageNo, Integer pageSize) {
        String hql = "from AppExtension obj where obj.appId=?1";
        return this.pageList(hql, pageNo, pageSize, appId);
    }

}
