package com.lsxy.app.portal.test;

import com.lsxy.app.portal.MainClass;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by liups on 2016/12/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainClass.class)
public class InsertAppTelNum {
    static {
//将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;
    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Autowired
    ResourcesRentService resourcesRentService;


    @Test
    public void insertNumToApp() throws InvocationTargetException, IllegalAccessException {
        ResourceTelenum num = resourceTelenumService.findById("8a2a6a4a58d33f530158d37637f700b2");
        ResourcesRent rent = resourcesRentService.findById("8a2a6a4a58d33eae0158d37e765800db");
        TelnumToLineGateway ttg = telnumToLineGatewayService.findById("8a2a6a4a58d33f530158d3761429005b");
        for(int i = 100;i<500;i++){
            String telNum = "10004" + i;
            //插入号码
            ResourceTelenum insertNum = new ResourceTelenum();
            BeanUtils.copyProperties(insertNum,num);
            insertNum.setId(null);
            insertNum.setTelNumber(telNum);
            insertNum.setCallUri(telNum);
            insertNum.setCreateTime(new Date());
            insertNum.setLastTime(new Date());
            resourceTelenumService.save(insertNum);
            //插入线路关系
            TelnumToLineGateway insertTtg = new TelnumToLineGateway();
            BeanUtils.copyProperties(insertTtg,ttg);
            insertTtg.setId(null);
            insertTtg.setTelNumber(telNum);
            insertTtg.setCreateTime(new Date());
            insertTtg.setLastTime(new Date());
            telnumToLineGatewayService.save(insertTtg);
            //插入号码租用关系
            ResourcesRent insertRent = new ResourcesRent();
            BeanUtils.copyProperties(insertRent,rent);
            insertRent.setId(null);
            insertRent.setResData(telNum);
            insertRent.setResourceTelenum(insertNum);
            insertRent.setCreateTime(new Date());
            insertRent.setLastTime(new Date());
            resourcesRentService.save(insertRent);
        }
    }
}
