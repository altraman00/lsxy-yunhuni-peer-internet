package com.lsxy.framework.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.VoiceTimeUse;
import com.lsxy.framework.api.consume.service.VoiceTimeUseService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.VoiceTimeUseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/30.
 */
@Service
public class VoiceTimeUseServiceImpl extends AbstractService<VoiceTimeUse> implements VoiceTimeUseService {
    @Autowired
    VoiceTimeUseDao voiceTimeUseDao;
    @Override
    public BaseDaoInterface<VoiceTimeUse, Serializable> getDao() {
        return this.voiceTimeUseDao;
    }
}
