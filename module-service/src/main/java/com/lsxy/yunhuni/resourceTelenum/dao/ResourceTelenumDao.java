package com.lsxy.yunhuni.resourceTelenum.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 全局号码DAO
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourceTelenumDao  extends BaseDaoInterface<ResourceTelenum, Serializable> {
    /**
     * 获取50个空闲的号码
     * @param status
     * @return
     */
    List<ResourceTelenum> findFirst50ByStatus(Integer status);
    /**
     * 根据号码获取资源
     * @param telNumber
     * @return
     */
    ResourceTelenum findByTelNumber(String telNumber);

    /**
     * 清除过期的号码
     * @param expireTime 过期时间
     */
    @Modifying
    @Query(value = "UPDATE db_lsxy_bi_yunhuni.tb_bi_yy_resource_telenum num SET num.status=0 , num.tenant_id=NULL WHERE num.id IN " +
            "(SELECT rent.res_id FROM db_lsxy_bi_yunhuni.tb_bi_resources_rent rent WHERE rent.rent_expire<:expireTime AND rent.res_type=1 AND rent.rent_status IN (1,2))",nativeQuery = true)
    void cleanExpireResourceTelnum(@Param("expireTime") Date expireTime);

    /**
     * 获取1个空闲的号码
     * @param status
     * @return
     */
    ResourceTelenum findFirstByStatus(int status);
}
