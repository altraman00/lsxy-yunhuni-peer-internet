package com.lsxy.yunhuni.resourceTelenum.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;

import java.io.Serializable;
import java.util.List;

import static javafx.scene.input.KeyCode.R;

/**
 * 全局号码DAO
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourceTelenumDao  extends BaseDaoInterface<ResourceTelenum, Serializable> {
    List<ResourceTelenum> findFirst50ByStatus(Integer status);

    ResourceTelenum findByTelNumber(String telNumber);
}
