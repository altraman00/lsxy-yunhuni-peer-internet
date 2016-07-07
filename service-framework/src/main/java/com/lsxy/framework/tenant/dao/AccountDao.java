package com.lsxy.framework.tenant.dao;



import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Account;

import java.io.Serializable;

/**
 * Created by Tandy on 2016/6/24.
 */
public interface AccountDao extends BaseDaoInterface<Account, Serializable> {
    Account findById(String id);

    /**
     * 根据用户名来查找账号
     * @param userName 用户名
     * @return
     */
    Account findByUserName(String userName);

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
     =* @param status 账号状态
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
}
