package com.lsxy.framework.consume.service;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.ConsumeDay;
import com.lsxy.framework.api.consume.service.ConsumeDayService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.ConsumeDayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 消费日统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ConsumeDayServiceImpl extends AbstractService<ConsumeDay> implements ConsumeDayService {
    @Autowired
    ConsumeDayDao consumeDayDao;
    @Override
    public BaseDaoInterface<ConsumeDay, Serializable> getDao() {
        return consumeDayDao;
    }
}
