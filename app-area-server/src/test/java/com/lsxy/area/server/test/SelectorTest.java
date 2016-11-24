package com.lsxy.area.server.test;

import com.lsxy.area.api.exceptions.AppOffLineException;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.AreaServerMainClass;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2016/10/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AreaServerMainClass.class)
public class SelectorTest {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    AreaAndTelNumSelector areaAndTelNumSelector;
    @Autowired
    AppService appService;

    @Test
    public void ttttt() throws AppOffLineException, UnsupportedEncodingException {
//        App app = appService.findById("40288ac95778fd6f01577909260d0000");
//        AreaAndTelNumSelector.Selector selector = areaAndTelNumSelector.getTelnumberAndAreaId(app, null, "02082241195");
//        System.out.println(selector.getOneTelnumber().getTelNumber());
//        String s = JSONUtil.objectToJson(selector.getToNum());
//        List<Map<String, String>> maps = JSONUtil.parseList(s);
//        System.out.println(JSONUtil.objectToJson(maps));
    }

}
