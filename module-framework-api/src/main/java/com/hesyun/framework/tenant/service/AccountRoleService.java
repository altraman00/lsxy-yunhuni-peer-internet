package com.hesyun.framework.tenant.service;

import com.lsxy.framework.core.BaseService;
import com.hesyun.framework.tenant.model.AccountRole;

import java.util.List;

public interface AccountRoleService extends BaseService<AccountRole> {


	void applyRoleToAccount(String accountId, String roleCode);

	List<AccountRole> findByAccount(String accountId);
}
