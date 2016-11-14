package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.dao.AppExtensionDao;
import com.lsxy.call.center.states.state.ExtensionState;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.ExtensionBindingToAgentException;
import com.lsxy.framework.core.exceptions.api.ExtensionUserExistException;
import com.lsxy.framework.core.exceptions.api.RequestIllegalArgumentException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    ExtensionState extensionState;

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
    public String register(AppExtension appExtension) throws YunhuniApiException {
        if(appExtension == null){
            throw new NullPointerException();
        }
        if(StringUtil.isBlank(appExtension.getTenantId())){
            throw new NullPointerException();
        }
        if(StringUtil.isBlank(appExtension.getAppId())){
            throw new NullPointerException();
        }
        if(StringUtil.isBlank(appExtension.getUser())){
            throw new NullPointerException();
        }
        if(StringUtil.isBlank(appExtension.getPassword())){
            throw new NullPointerException();
        }
        if(StringUtil.isBlank(appExtension.getType())){
            throw new NullPointerException();
        }
        if(this.exists(appExtension.getUser())){
            throw new ExtensionUserExistException();
        }
        this.save(appExtension);
        //TODO 初始化状态状态
        extensionState.setLastRegisterStatus(appExtension.getId(),200);

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
    public void delete(String extensionId, String appId) throws YunhuniApiException{
        AppExtension extension = this.findById(extensionId);
        if(StringUtils.isNotBlank(appId) && appId.equals(extension.getAppId())){
            try {
                String agent = extensionState.getAgent(extensionId);
                if(StringUtils.isBlank(agent)){
                    this.delete(extension);
                    redisCacheService.del(extensionId);
                }else{
                    throw new ExtensionBindingToAgentException();
                }
            } catch (Exception e) {
                logger.error("删除分机失败",e);
                throw new RequestIllegalArgumentException();
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


}
