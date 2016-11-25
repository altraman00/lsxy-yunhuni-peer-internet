package com.lsxy.app.opensips.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.app.opensips.dao.LocationDao;
import com.lsxy.app.opensips.dao.SubscriberDao;
import com.lsxy.call.center.api.opensips.service.OpensipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liups on 2016/11/25.
 */
@Component
@Service
public class OpensipsServiceImpl implements OpensipsService {
    @Autowired
    LocationDao locationDao;
    @Autowired
    SubscriberDao subscriberDao;


    @Override
    public void createExtension(String username, String password) {

    }
}
