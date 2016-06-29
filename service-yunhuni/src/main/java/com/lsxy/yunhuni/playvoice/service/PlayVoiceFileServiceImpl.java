package com.lsxy.yunhuni.playvoice.service;

import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.core.service.AbstractService;
import com.lsxy.yuhuni.playvoice.model.PlayVoiceFile;
import com.lsxy.yuhuni.playvoice.service.PlayVoiceFileService;
import com.lsxy.yunhuni.playvoice.dao.PlayVoiceFileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class PlayVoiceFileServiceImpl extends AbstractService<PlayVoiceFile> implements PlayVoiceFileService {
    @Autowired
    private PlayVoiceFileDao playVoiceFileDao;
    @Override
    public BaseDaoInterface<PlayVoiceFile, Serializable> getDao() {
        return this.playVoiceFileDao;
    }

}
