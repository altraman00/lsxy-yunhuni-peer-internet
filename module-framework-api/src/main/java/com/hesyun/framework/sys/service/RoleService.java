package com.hesyun.framework.sys.service;

import java.util.List;

import com.lsxy.framework.core.BaseService;
import com.hesyun.framework.sys.model.Role;

/**
 * 租户角色
 * 
 * @author WangYun
 *
 */
public interface RoleService extends BaseService<Role> {

	Role findRoleByRoleCode(String roleCode);

	List<Role> findAll();

} 
