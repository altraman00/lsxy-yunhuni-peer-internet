package com.lsxy.framework.tenant.dao;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Tandy on 2016/6/24.
 */
public interface AccountDao extends BaseDaoInterface<Account, Serializable> {
    Account findById(String id);
    /**
     * 根据状态获取用户
     * @param status
     * @return
     */
    List<Account> findByStatus(Integer status);
    /**
     * 根据用户名来查找账号
     * @param userName 用户名
     * @param status 账号状态
     * @return
     */
    Account findByUserNameAndStatus(String userName,int status);

    /**
     * 查找用户名是否存在
     * @param userName 用户名
     * @param status 账号状态
     * @return
     */
    Long countByUserNameAndStatus(String userName,int status);
    /**
     * 查找手机号是否存在
     * @param mobile 手机
     * @param status 账号状态
     * @return
     */
    Long countByMobileAndStatus(String mobile,int status);
    /**
     * 查找邮箱是否存在
     * @param email 邮箱
     * @param status 账号状态
     * @return
     */
    Long countByEmailAndStatus(String email,int status);

    /**
     * 根据邮箱查找账号
     * @param email 邮箱
     * @param status 状态
     * @return
     */
    Account findByEmailAndStatus(String email,int status);

    /**
     * 根据手机查找账号
     * @param mobile 手机
     * @param status 状态
     * @return
     */
    Account findByMobileAndStatus(String mobile,int status);

    /**
     * 清除过期的注册账号
     * @param expireTime 在这个时间以前的，未被激活的账号会被设为过期
     */
    @Modifying
    @Query("update Account a set a.status=:expireStatus where a.status=:notActivityStatus and a.createTime < :expireTime")
    void cleanExpireRegisterAccount(@Param("expireStatus")int expireStatus,@Param("notActivityStatus")int notActivityStatus, @Param("expireTime") Date expireTime);

    @Query("from Account a where a.tenant.id = :tenantId")
    List<Account> findByTenantId(@Param("tenantId") String tenantId);
}
