package com.lsxy.app.oc.rest.tenant.vo;


import com.lsxy.call.center.api.model.Condition;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2017/3/1.
 */
@ApiModel
public class ConditionVo {
    @ApiModelProperty(name = "id",value = "排队条件ID")
    private String id;
    @ApiModelProperty(name = "certId",value = " 关联子账号-鉴权账户")
    private String certId;
    @ApiModelProperty(name = "subaccountId",value = " 关联子账号")
    private String subaccountId;
    @ApiModelProperty(name = "whereExpression",value = "条件选择表达式")
    private String whereExpression;
    @ApiModelProperty(name = "sortExpression",value = "排序表达式")
    private String sortExpression;
    @ApiModelProperty(name = "priority",value = "优先级")
    private Integer priority;
    @ApiModelProperty(name = "queueTimeout",value = "等待超时时间（秒）")
    private Integer queueTimeout;
    @ApiModelProperty(name = "fetchTimeout",value = " 接听超时时间（秒）")
    private Integer fetchTimeout;
    @ApiModelProperty(name = "remark",value = "备注")
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
