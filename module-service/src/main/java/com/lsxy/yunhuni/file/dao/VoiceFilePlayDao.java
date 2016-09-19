package com.lsxy.yunhuni.file.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;

import java.io.Serializable;
import java.util.List;

/**
 * 放音文件
 * Created by zhangxb on 2016/7/21.
 */
public interface VoiceFilePlayDao extends BaseDaoInterface<VoiceFilePlay,Serializable> {
    List<VoiceFilePlay> findByAppId(String appId);
}
