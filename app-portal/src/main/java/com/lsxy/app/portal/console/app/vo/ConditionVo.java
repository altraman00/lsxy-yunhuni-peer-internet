package com.lsxy.app.portal.console.app.vo;


import com.lsxy.call.center.api.model.Condition;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;

/**
 * Created by zhangxb on 2017/3/1.
 */
public class ConditionVo {
    private String id;

    private String certId;

    private String subaccountId;

    private String whereExpression;

    private String sortExpression;

    private Integer priority;

    private Integer queueTimeout;

    private Integer fetchTimeout;

    private String remark;

    public ConditionVo(String id, String certId, String subaccountId, String whereExpression, String sortExpression, Integer priority, Integer queueTimeout, Integer fetchTimeout, String remark) {
        this.id = id;
        this.certId = certId;
        this.subaccountId = subaccountId;
        this.whereExpression = whereExpression;
        this.sortExpression = sortExpression;
        this.priority = priority;
        this.queueTimeout = queueTimeout;
        this.fetchTimeout = fetchTimeout;
        this.remark = remark;
    }
    public static ConditionVo changeConditionToConditionVO(Condition condition, ApiCertificateSubAccount apiCertificateSubAccount){
        String certId = apiCertificateSubAccount!=null ? apiCertificateSubAccount.getCertId() :"";
        return new ConditionVo(condition.getId(),certId,condition.getSubaccountId(),condition.getWhereExpression(),condition.getSortExpression(),condition.getPriority(),condition.getQueueTimeout(),condition.getFetchTimeout(),condition.getRemark());
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    public String getWhereExpression() {
        return whereExpression;
    }

    public void setWhereExpression(String whereExpression) {
        this.whereExpression = whereExpression;
    }

    public String getSortExpression() {
        return sortExpression;
    }

    public void setSortExpression(String sortExpression) {
        this.sortExpression = sortExpression;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getQueueTimeout() {
        return queueTimeout;
    }

    public void setQueueTimeout(Integer queueTimeout) {
        this.queueTimeout = queueTimeout;
    }

    public Integer getFetchTimeout() {
        return fetchTimeout;
    }

    public void setFetchTimeout(Integer fetchTimeout) {
        this.fetchTimeout = fetchTimeout;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
