package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.session.model.VoiceCallback;
import com.lsxy.yunhuni.api.session.service.VoiceCallbackService;
import com.lsxy.yunhuni.session.dao.VoiceCallbackDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/9/6.
 */
@Service
public class VoiceCallbackServiceImpl extends AbstractService<VoiceCallback> implements VoiceCallbackService {
    @Autowired
    VoiceCallbackDao voiceCallbackDao;
    @Override
    public BaseDaoInterface<VoiceCallback, Serializable> getDao() {
        return this.voiceCallbackDao;
    }
}
