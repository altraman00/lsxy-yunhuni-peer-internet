package com.lsxy.app.opensips.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.app.opensips.dao.LocationDao;
import com.lsxy.app.opensips.dao.SubscriberDao;
import com.lsxy.call.center.api.opensips.model.Location;
import com.lsxy.call.center.api.opensips.model.Subscriber;
import com.lsxy.call.center.api.opensips.service.OpensipsService;
import com.lsxy.framework.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by liups on 2016/11/25.
 */
@Component
@Service
public class OpensipsServiceImpl implements OpensipsService {
    private static final String domain = SystemConfig.getProperty("global.opensips.domain");
    @Autowired
    LocationDao locationDao;
    @Autowired
    SubscriberDao subscriberDao;

    @Override
    public void createExtension(String username, String password) {
        String ha1 = getMD5(username + ":" + domain + ":" + password);
        String ha1b = getMD5(username + "@" + domain + ":" + domain + ":" + password);
        Subscriber subscriber = new Subscriber(username,domain,password,ha1,ha1b);
        subscriberDao.save(subscriber);
    }

    /**
     * 对字符串md5加密
     * @param str
     * @return
     */
    private static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密出现错误",e);
        }
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
