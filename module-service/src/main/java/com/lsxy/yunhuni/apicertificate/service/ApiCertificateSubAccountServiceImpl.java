package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuotaType;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.apicertificate.dao.ApiCertificateSubAccountDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by liups on 2017/2/14.
 */
@Service
public class ApiCertificateSubAccountServiceImpl extends AbstractService<ApiCertificateSubAccount> implements ApiCertificateSubAccountService {
    @Autowired
    private ApiCertificateSubAccountDao apiCertificateSubAccountDao;
    @Autowired
    private AppService appService;
    @Autowired
    private ApiCertificateService apiCertificateService;
    @Autowired
    private CertAccountQuotaService certAccountQuotaService;
    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<ApiCertificateSubAccount, Serializable> getDao() {
        return this.apiCertificateSubAccountDao;
    }

    @Override
    public List<ApiCertificateSubAccount> getListByCerbId(String certId) {
        String hql = " from ApiCertificateSubAccount obj where obj.certId like '%"+certId+"%' ";
        return this.list(hql);
    }

    @Override
    public ApiCertificateSubAccount findByCerbId(String certId) {
        return apiCertificateSubAccountDao.findByCertId(certId);
    }

    @Override
    public ApiCertificateSubAccount createSubAccount(ApiCertificateSubAccount subAccount) {
        App app = appService.findById(subAccount.getAppId());
        if(app == null){
            //TODO
            throw new RuntimeException("应用不存在");
        }
        String tenantId = app.getTenant().getId();
        ApiCertificate cert = apiCertificateService.findApiCertificateByTenantId(tenantId);
        subAccount.setTenantId(tenantId);
        subAccount.setType(ApiCertificate.TYPE_SUBACCOUNT);
        subAccount.setCertId(UUIDGenerator.uuid());
        subAccount.setSecretKey(UUIDGenerator.uuid());
        subAccount.setAppId(subAccount.getAppId());
        subAccount.setCallbackUrl(subAccount.getCallbackUrl());
        subAccount.setParentId(cert.getId());
        subAccount.setRemark(subAccount.getRemark());
        subAccount.setExtensionPrefix(appService.getExtensionPrefixNum());
        this.save(subAccount);

        List<CertAccountQuota> subQuotas = new ArrayList<>();
        List<CertAccountQuota> quotas = subAccount.getQuotas();
        Collection<CertAccountQuota> setQuotas = getQuotasByApp(app,quotas);
        if(setQuotas != null){
            for(CertAccountQuota quota : setQuotas){
                CertAccountQuota saveQ = new CertAccountQuota(tenantId,subAccount.getAppId(),subAccount.getId(),quota.getType(),quota.getValue(),quota.getRemark());
                certAccountQuotaService.save(saveQ);
                subQuotas.add(saveQ);
            }
        }
        subAccount.setQuotas(subQuotas);
        return subAccount;
    }

    private Collection<CertAccountQuota> getQuotasByApp(App app, List<CertAccountQuota> quotas) {
        Map<String,CertAccountQuota> map = new HashMap();
        if(quotas!=null){
            for(CertAccountQuota quota : quotas){ 
                if(StringUtils.isNotBlank(quota.getType()) && map.get(quota.getType()) == null){
                    map.put(quota.getType(),quota);
                }
            }
        }
        Set<String> keys = map.keySet();
        if(app.PRODUCT_MSG.equals(app.getType())){
            //消息类配额
            if(!keys.contains(CertAccountQuotaType.SmsQuota)){
                map.put(CertAccountQuotaType.SmsQuota.name(),new CertAccountQuota(CertAccountQuotaType.SmsQuota.name()));
            }
            if(!keys.contains(CertAccountQuotaType.UssdQuota)){
                map.put(CertAccountQuotaType.UssdQuota.name(),new CertAccountQuota(CertAccountQuotaType.UssdQuota.name()));
            }
        }else{
            if(!keys.contains(CertAccountQuotaType.CallQuota.name())){
                map.put(CertAccountQuotaType.CallQuota.name(),new CertAccountQuota(CertAccountQuotaType.CallQuota.name()));
            }
            if(app.getType().equals(App.PRODUCT_CALL_CENTER)){
                if(!keys.contains(CertAccountQuotaType.AgentQuota.name())){
                    map.put(CertAccountQuotaType.AgentQuota.name(),new CertAccountQuota(CertAccountQuotaType.AgentQuota.name()));
                }
            }
        }
        return map.values();
    }

    @Override
    public void deleteSubAccount(String tenantId,String appId,String subAccountId) throws InvocationTargetException, IllegalAccessException {
        ApiCertificateSubAccount subAccount = this.findById(subAccountId);
        if(subAccount.getTenantId().equals(tenantId) && subAccount.getAppId().equals(appId)){
            resourceTelenumService.subaccountUnbindAll(tenantId,appId,subAccountId);
        }
        this.delete(subAccount);
    }

    @Override
    public void updateSubAccount(ApiCertificateSubAccount subAccount) {
        ApiCertificateSubAccount saveAccount = this.findById(subAccount.getId());
        if(saveAccount.getAppId().equals(subAccount.getAppId())){
            saveAccount.setCallbackUrl(subAccount.getCallbackUrl());
            saveAccount.setEnabled(subAccount.getEnabled());
            saveAccount.setRemark(saveAccount.getRemark());
            this.save(saveAccount);
        }
        certAccountQuotaService.updateQuotas(subAccount.getId(),subAccount.getQuotas());
    }

    @Override
    public ApiCertificateSubAccount findByIdWithQuota(String subAccountId) {
        ApiCertificateSubAccount subAccount = this.findById(subAccountId);
        List<CertAccountQuota> quotas = certAccountQuotaService.findByCertAccountId(subAccount.getId());
        for(CertAccountQuota quota : quotas){
            certAccountQuotaService.getCurrentQuota(quota);
        }
        subAccount.setQuotas(quotas);
        return subAccount;
    }

    @Override
    public Page<ApiCertificateSubAccount> pageListWithQuota(String appId, int pageNo, int pageSize) {
        String hql = "from ApiCertificateSubAccount obj where obj.appId = ?1";
        Page<ApiCertificateSubAccount> page = this.pageList(hql, pageNo, pageSize, appId);
        List<ApiCertificateSubAccount> result = page.getResult();
        List<String> ids = new ArrayList<>();
        if(result != null){
            for(ApiCertificateSubAccount subAccount : result){
                ids.add(subAccount.getId());
            }
        }
        List<CertAccountQuota> quotas = certAccountQuotaService.findByCertAccountIds(ids);
        for(CertAccountQuota quota : quotas){
            //获取配额实时的数据
            certAccountQuotaService.getCurrentQuota(quota);
        }

        Map<String,List<CertAccountQuota>> map = new HashMap();
        for(CertAccountQuota quota : quotas){
            String certAccountId = quota.getCertAccountId();
            List<CertAccountQuota> quotaList = map.get(certAccountId);
            if(quotaList == null){
                quotaList = new ArrayList<>();
                map.put(certAccountId,quotaList);
            }
            quotaList.add(quota);
        }
        if(result != null){
            for(ApiCertificateSubAccount subAccount: result){
                subAccount.setQuotas(map.get(subAccount.getId()));
            }
        }
        return page;
    }

    @Override
    public Page<ApiCertificateSubAccount> pageListWithNotQuota(String appId, int pageNo, int pageSize) {
        String hql = "from ApiCertificateSubAccount obj where obj.appId = ?1";
        Page<ApiCertificateSubAccount> page = this.pageList(hql, pageNo, pageSize, appId);
        return page;
    }

    @Override
    public Page<ApiCertificateSubAccount> pageListWithQuotaByCondition(String appId, int pageNo, int pageSize, String certId, String remark, Integer enabled) {
        String hql = "from ApiCertificateSubAccount obj where obj.appId = ?1";
        if(StringUtils.isNotEmpty(certId)){
            hql += " and obj.certId='"+certId+"' ";
        }
        if(StringUtils.isNotEmpty(remark)){
            hql += " and obj.certId like '%"+certId+"%' ";
        }
        if(enabled != null){
            hql += " and obj.enabled = '"+enabled+"' ";
        }
        Page<ApiCertificateSubAccount> page = this.pageList(hql, pageNo, pageSize, appId);
        List<CertAccountQuota> quotas = certAccountQuotaService.findByAppId(appId);
        for(CertAccountQuota quota : quotas){
            //获取配额实时的数据
            certAccountQuotaService.getCurrentQuota(quota);
        }
        Map<String,List<CertAccountQuota>> map = new HashMap();
        for(CertAccountQuota quota : quotas){
            String certAccountId = quota.getCertAccountId();
            List<CertAccountQuota> quotaList = map.get(certAccountId);
            if(quotaList == null){
                quotaList = new ArrayList<>();
                map.put(certAccountId,quotaList);
            }
            quotaList.add(quota);
        }
        List<ApiCertificateSubAccount> result = page.getResult();
        if(result != null){
            for(ApiCertificateSubAccount subAccount: result){
                subAccount.setQuotas(map.get(subAccount.getId()));
            }
        }
        return page;
    }

    @Override
    public List<ApiCertificateSubAccount> findByAppId(String appId) {
        return apiCertificateSubAccountDao.findByAppId(appId);
    }

    @Override
    public boolean subaccountCheck(String sub1,String sub2){
        if(sub1 == null && sub2 == null){
            return true;
        }
        if(sub1 != null && sub2 == null){
            return false;
        }
        if(sub1 == null && sub2 != null){
            return false;
        }
        return sub1.equals(sub2);
    }

}
