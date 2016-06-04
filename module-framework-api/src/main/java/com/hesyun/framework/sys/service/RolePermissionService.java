package com.hesyun.framework.sys.service;

import java.util.List;

import com.lsxy.framework.core.BaseService;
import com.hesyun.framework.sys.model.RolePermission;

/**
 * 系统权限
 * 
 * @author WangYun
 *
 */
public interface RolePermissionService extends BaseService<RolePermission> {

	List<RolePermission> findRolePermissions(String roleid);


}
