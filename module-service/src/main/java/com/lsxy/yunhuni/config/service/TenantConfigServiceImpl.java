package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.GlobalConfig;
import com.lsxy.yunhuni.api.config.model.TenantConfig;
import com.lsxy.yunhuni.api.config.service.GlobalConfigService;
import com.lsxy.yunhuni.api.config.service.TenantConfigService;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.config.dao.TenantConfigDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局配置类impl
 * Created by zhangxb on 2016/8/23.
 */
@Service
public class TenantConfigServiceImpl extends AbstractService<TenantConfig> implements TenantConfigService {
    @Autowired
    private TenantConfigDao tenantConfigDao;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Override
    public BaseDaoInterface<TenantConfig, Serializable> getDao() {
        return this.tenantConfigDao;
    }

    @Override
    public TenantConfig findByTypeAndKeyNameAndTenantIdAndAppId(String type, String keyName, String tenantId,String appId) {
        return tenantConfigDao.findByTypeAndKeyNameAndTenantIdAndAppId(type,keyName,tenantId,appId);
    }

    @Override
    public List<TenantConfig> getPageByTypeAndKeyName(String type, String keyName) {
        String hql = " FROM TenantConfig obj WHERE obj.type=?1 and obj.keyName=?2 ";
        return this.list(hql,type,keyName);
    }

    @Override
    public int getRecordingTimeByTenantIdAndAppId(String tenantId, String appId) {
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
        TenantConfig tenantConfig = findByTypeAndKeyNameAndTenantIdAndAppId(GlobalConfig.TYPE_RECORDING, GlobalConfig.KEY_RECORDING, tenantId, appId);
        if(tenantConfig!=null) {
            Matcher matcher1 = pattern.matcher(tenantConfig.getValue());
            if (matcher1.matches()) {
                tenantRecording = Integer.valueOf(tenantConfig.getValue());
            }
            if (tenantRecording > globalRecording) {
                globalRecording = tenantRecording;
            }
        }
        return globalRecording;
    }

}
