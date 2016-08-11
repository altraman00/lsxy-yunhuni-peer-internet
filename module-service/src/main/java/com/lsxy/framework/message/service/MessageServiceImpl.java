package com.lsxy.framework.message.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.message.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

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

    @Override
    public Page<Message> pageList(Integer type,Integer status, String startTime, String endTime, Integer pageNo, Integer pageSize) {
        Date date1 = DateUtils.parseDate(startTime,"yyyy-MM-dd");
        Date date2 = DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        String hql = "from Message obj where obj.type=?1 and  obj.lineTime>=?2 and obj.lineTime<=?3 ";
        if(StringUtil.isNotEmpty(status+"")){
            hql+=" and obj.status='"+status+"' ";
        }
        Page<Message> page =  this.pageList(hql,pageNo,pageSize,type,date1,date2);
        return page;
    }
}
