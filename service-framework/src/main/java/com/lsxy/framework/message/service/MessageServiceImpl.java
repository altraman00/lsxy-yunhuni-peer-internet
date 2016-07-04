package com.lsxy.framework.message.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.message.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 消息实现类
 * Created by zhangxb on 2016/7/4.
 */
@Service
public class MessageServiceImpl extends AbstractService<Message> implements MessageService{

    @Autowired
    MessageDao messageDao;
    @Override
    public BaseDaoInterface<Message, Serializable> getDao() {
        return messageDao;
    }
}
