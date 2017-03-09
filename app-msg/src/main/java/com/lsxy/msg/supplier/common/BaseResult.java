package com.lsxy.msg.supplier.common;

import com.lsxy.framework.core.utils.JSONUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2017/1/10.
 */
public class BaseResult implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String resultCode;
    protected String resultDesc;
    protected String handlers;
    protected String taskId;
    public BaseResult() {
    }
    public BaseResult(ResultCode result) {
        this.resultCode = result.getResultCode();
        this.resultDesc = result.getResultDesc();
    }
    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getHandlers() {
        return handlers;
    }

    public void setHandlers(String handlers) {
        this.handlers = handlers;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        Map map = new HashMap();
        map.put("result_code",this.resultCode);
        if(StringUtils.isNotEmpty(this.resultDesc)) {
            map.put("result_desc",this.resultDesc );
        }
        return JSONUtil.objectToJson(map);
    }
    public String toString2() {
        return JSONUtil.objectToJson(this);
    }

}
