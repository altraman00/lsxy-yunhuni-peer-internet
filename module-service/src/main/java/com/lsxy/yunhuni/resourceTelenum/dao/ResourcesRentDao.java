package com.lsxy.yunhuni.resourceTelenum.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 呼入号码DAO
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourcesRentDao extends BaseDaoInterface<ResourcesRent, Serializable> {

    /**
     * 根据资源ID和状态获取资源租用关系
     * @param id
     * @param status
     * @return
     */
    ResourcesRent findByResourceTelenumIdAndRentStatus(String id, int status);

    ResourcesRent findByResDataAndRentStatus(String resData, int status);

    /**
     * 根据状态获取租户的号码
     * @param id
     * @param status
     * @return
     */
    List<ResourcesRent> findByTenantIdAndRentStatus(String id, int status);

    /**
     * 清除过期时间
     * @param expireTime
     */
    @Modifying
    @Query("update ResourcesRent rent set rent.rentStatus=3 where rent.rentExpire<:expireTime and rent.resType=1 and rent.rentStatus in (1,2)")
    void cleanExpireTelnumResourceRent(@Param("expireTime") Date expireTime);

    /**
     * 获取过期但没被释放的号码资源
     * @param date
     * @return
     */
    @Query(value = "SELECT rent.id,rent.tenant_id ,rent.app_id FROM db_lsxy_bi_yunhuni.tb_bi_resources_rent rent " +
            "WHERE ( rent.deleted=0) AND rent.rent_status in (1,2) AND rent.res_type=1 AND rent.rent_expire<:expireTime",nativeQuery = true)
    List<Object[]> findInfoExpireRent(@Param("expireTime") Date date);

    /**
     * 号码续费过期时间延长
     * @param expireTime
     */
    @Modifying
    @Query("update ResourcesRent rent set rent.rentExpire=:expireTime where rent.id=:id")
    void updateResourceRentExpireTime(@Param("id") String id,@Param("expireTime") Date expireTime);

    /**
     * 应用释放所有的号码
     * @param tenantId
     * @param appId
     */
    @Modifying
    @Query(value = "update db_lsxy_bi_yunhuni.tb_bi_resources_rent rent set rent.app_id = null,rent.rent_status = 2,rent.last_time = :date where rent.tenant_id = :tenantId and rent.app_id = :appId and rent.deleted = 0 and rent.rent_status = 1",nativeQuery = true)
    void appUnbindAll(@Param("tenantId") String tenantId, @Param("appId") String appId,@Param("date") Date date);

}
