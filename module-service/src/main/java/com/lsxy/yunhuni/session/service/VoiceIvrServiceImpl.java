package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import com.lsxy.yunhuni.session.dao.VoiceIvrDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/7/19.
 */
@Service
public class VoiceIvrServiceImpl extends AbstractService<VoiceIvr> implements VoiceIvrService {
    @Autowired
    private VoiceIvrDao voiceIvrDao;
    @Override
    public BaseDaoInterface<VoiceIvr, Serializable> getDao() {
        return voiceIvrDao;
    }

}
