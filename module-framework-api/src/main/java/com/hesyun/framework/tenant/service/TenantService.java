package com.hesyun.framework.tenant.service;

import com.hesyun.framework.tenant.model.Tenant;
import com.lsxy.framework.core.AbstractManager;
import com.lsxy.framework.core.BaseService;
import com.lsxy.framework.core.utils.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 租户Mananger
 * 
 * @author WangYun
 *
 */
@SuppressWarnings("rawtypes")
public interface TenantService extends BaseService<Tenant> {

	public Tenant findTenantByTenantUn( String tenantUn);
	
	
	public int getNextTenantUid();
	
	public AbstractManager<Tenant> getTenantManager(); 

	public Tenant registerTenant(Map tenantJsonMap, int tenantTypeCommon,
								 String tenantUn);

	public Tenant testTenant(Tenant tenant);

	public void deleteTenant(String tenantUn);

	public Tenant findTenantByHsyOpenid(String openid);

	public List<Tenant> findCheckedTenant();

	public boolean isAttrUnique(String attrName, String attrValue, String id);

	public Collection<? extends String> findAllTenantUns();

	public void initTenantDefault(Map initMap, Tenant tenant);
	
	Tenant removeTenantByCustUid(String tenantUn, String uid);
	
	public AbstractManager<Tenant> tenantManager();

	List<Tenant> getCommonTenants();

	Page searchTenants(String searchName, int pageNo, int pageSize);

}
