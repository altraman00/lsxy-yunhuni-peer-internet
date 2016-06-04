package com.hesyun.framework.tenant.service;

import com.lsxy.framework.core.BaseService;
import com.hesyun.framework.tenant.model.TenantContact;

import java.util.List;

/**
 * 租户联系人Mananger
 *
 * @author WangYun
 */
public interface TenantContactService extends BaseService<TenantContact> {

	TenantContact findByEmail(String email);

	boolean isMobileExist(String mobile);

	void batchDeleteByTenantId(String tenantId);

	List<TenantContact> findByTenantUn(String tenantUn);


	/**
	 * 找到指定商户的第一个联系人对象
	 * @param tenantUn
	 * @return
	 */
	TenantContact findFirstContact(String tenantUn);
}
