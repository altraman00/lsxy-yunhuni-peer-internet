package com.lsxy.framework.tenant.model;

/**
 * Created by Tandy on 2016/6/7.
 * 租户角色
 * 暂时只有一个角色，租户用户 TENANT_USER 在租户功能范围内拥有最大权限
 */
public class TenantRole {

    public static final String ROLE_TENANT_USER="TENANT_USER";

    private String roleName;

    public TenantRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public static TenantRole tenantUserRole(){
        return new TenantRole(ROLE_TENANT_USER);
    }
}
