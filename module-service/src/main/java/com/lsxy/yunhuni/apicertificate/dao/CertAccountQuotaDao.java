package com.lsxy.yunhuni.apicertificate.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2017/2/15.
 */
public interface CertAccountQuotaDao extends BaseDaoInterface<CertAccountQuota, Serializable> {
    @Modifying
    @Query(value = "update db_lsxy_bi_yunhuni.tb_bi_cert_account_quota q set q.value = :value where q.cert_account_id = :certAccountId and q.type = :type and q.deleted = 0 ",nativeQuery = true)
    void updateQuota(@Param(value = "certAccountId")String certAccountId, @Param(value = "type")String type, @Param(value = "value")Long value);

    List<CertAccountQuota> findByCertAccountId(String id);
    List<CertAccountQuota> findByAppId(String appId);

    CertAccountQuota findByCertAccountIdAndType(String certAccountId, String type);
}
