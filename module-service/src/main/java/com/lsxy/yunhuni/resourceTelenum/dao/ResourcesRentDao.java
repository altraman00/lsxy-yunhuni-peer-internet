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
     * 根据AppId查找租用关系
     * @param appId
     * @param status
     * @return
     */
    List<ResourcesRent> findByAppIdAndRentStatus(String appId, int status);

    /**
     * 根据资源ID和状态获取资源租用关系
     * @param id
     * @param status
     * @return
     */
    ResourcesRent findByResourceTelenumIdAndRentStatus(String id, int status);
    ResourcesRent findByResourceTelenumId(String id);
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
    @Modifying(clearAutomatically = true)
    @Query("update ResourcesRent rent set rent.app=null,rent.rentStatus=3 where rent.rentExpire<:expireTime and rent.resType=1 and rent.rentStatus in (1,2)")
    void cleanExpireTelnumResourceRent(@Param("expireTime") Date expireTime);

    /**
     * 获取过期但没被释放的号码资源
     * @param rentStatusRelease
     * @param restypeTelenum
     * @param date
     * @return
     */
    List<ResourcesRent> findByRentStatusNotAndResTypeAndRentExpireLessThan(int rentStatusRelease, String restypeTelenum, Date date);

    /**
     * 号码续费过期时间延长
     * @param expireTime
     */
    @Modifying(clearAutomatically = true)
    @Query("update ResourcesRent rent set rent.rentExpire=:expireTime where rent.id=:id")
    void updateResourceRentExpireTime(@Param("id") String id,@Param("expireTime") Date expireTime);

}
