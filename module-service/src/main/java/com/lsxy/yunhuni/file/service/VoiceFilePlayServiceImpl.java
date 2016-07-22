package com.lsxy.yunhuni.file.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import com.lsxy.yunhuni.file.dao.VoiceFilePlayDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 放音文件
 * Created by zhangxb on 2016/7/21.
 */
@Service
public class VoiceFilePlayServiceImpl extends AbstractService<VoiceFilePlay> implements VoiceFilePlayService{
    @Autowired
    private VoiceFilePlayDao voiceFilePlayDao;
    @Override
    public BaseDaoInterface<VoiceFilePlay, Serializable> getDao() {
        return voiceFilePlayDao;
    }

    @Override
    public Page<VoiceFilePlay> pageList(Integer pageNo, Integer pageSize, String name,String appId,String tenantId) {
        String hql = " from VoiceFilePlay obj where obj.app.id=?1 and obj.tenant.id=?2 ";
        Page<VoiceFilePlay> page = null;
        if(StringUtils.isEmpty(name)){
            page = this.pageList(hql,pageNo,pageSize,appId,tenantId);
        }else{
            hql = "from  VoiceFilePlay obj where obj.app.id=?1 and obj.tenant.id=?2 and obj.name like ?3 ";
            page = this.pageList(hql,pageNo,pageSize,appId,tenantId,"%"+name+"%");
        }
        return page;
    }
}
