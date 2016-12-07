package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.call.center.api.opensips.model.Location;
import com.lsxy.call.center.api.opensips.model.Subscriber;
import com.lsxy.call.center.api.opensips.service.OpensipsExtensionService;
import com.lsxy.call.center.dao.LocationDao;
import com.lsxy.call.center.dao.SubscriberDao;
import com.lsxy.framework.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * Created by liups on 2016/11/25.
 */
@Component
@Service
public class OpensipsExtensionServiceImpl implements OpensipsExtensionService {
    private static final String domain = SystemConfig.getProperty("app.cc.opensips.domain");
    @Autowired
    LocationDao locationDao;
    @Autowired
    SubscriberDao subscriberDao;

    @Override
    public void createExtension(String username, String password) {
        String ha1 = DigestUtils.md5DigestAsHex((username + ":" + domain + ":" + password).getBytes());
        String ha1b = DigestUtils.md5DigestAsHex((username + "@" + domain + ":" + domain + ":" + password).getBytes());
        Subscriber subscriber = new Subscriber(username,domain,password,"",ha1,ha1b);
        subscriberDao.save(subscriber);
    }

    @Override
    public void deleteExtension(String username) {
        Subscriber subscriber = subscriberDao.findByUsername(username);
        subscriberDao.delete(subscriber);
    }

    @Override
    public List<Location> getLocationsByUsername(String username) {
        return locationDao.findByUsername(username);
    }

}
