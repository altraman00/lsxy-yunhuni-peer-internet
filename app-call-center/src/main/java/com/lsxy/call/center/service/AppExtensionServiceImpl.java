package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.opensips.service.OpensipsService;
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
                if(StringUtil.isBlank(appExtension.getUser()) || StringUtil.isBlank(appExtension.getPassword())){
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
                throw new RequestIllegalArgumentException();
            case AppExtension.TYPE_TELPHONE:
                appExtension.setUser(null);
                appExtension.setPassword(null);
                if(StringUtil.isBlank(appExtension.getTelenum()) ){
                    throw new RequestIllegalArgumentException();
                }
                break;
            default:
                throw new RequestIllegalArgumentException();
        }

        this.save(appExtension);
        if(AppExtension.TYPE_SIP.equals(appExtension.getType())){
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
        if(StringUtils.isNotBlank(appId) && appId.equals(extension.getAppId())){
            try {
                String agent = extensionState.getAgent(extensionId);
                if(StringUtils.isBlank(agent)){
                    this.delete(extension);
                    if(AppExtension.TYPE_SIP.equals(extension.getType())){
                        opensipsService.deleteExtension(extension.getUser());
                    }
                    redisCacheService.del(extensionId);
                }else{
                    throw new ExtensionBindingToAgentException();
                }
            } catch (Exception e) {
                logger.error("删除分机失败:{}",extensionId);
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

    @Override
    public AppExtension findOne(String appId, String extensionId) {
        if(StringUtils.isBlank(appId)){
            logger.error("appId 不能为空");
            throw new IllegalArgumentException("appId 不能为空");
        }
        if(StringUtils.isBlank(extensionId)){
            logger.error("extension 不能为空");
            throw new IllegalArgumentException("extension 不能为空");
        }
        AppExtension extension = this.findById(extensionId);
        if(!appId.equals(extension.getAppId())){
            throw new IllegalArgumentException("extension不属于该App");
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
        extensionState.setAll(model);
    }


}
