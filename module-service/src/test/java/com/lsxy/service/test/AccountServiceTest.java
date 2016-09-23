package com.lsxy.service.test;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by Tandy on 2016/7/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(value={FrameworkServiceConfig.class, YunhuniServiceConfig.class, FrameworkApiConfig.class, YunhuniApiConfig.class, FrameworkCacheConfig.class})
@EnableJpaRepositories(value = {"com.lsxy.framework","com.lsxy.yunhuni"})
@EnableAutoConfiguration
@SpringApplicationConfiguration(AccountServiceTest.class)
public class AccountServiceTest {

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Autowired
    private AccountService accountService;

    @Autowired
    private ConsumeService consumeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public String getSystemId(){
        return "aa";
    }

    @Test
    public void test001() {
        Account account  = accountService.findAccountByUserName("user001");

        Assert.notNull(account);

//        account = accountService.findById("40288aca57406040015740625b670001");
//        Assert.notNull(account);

//        account.setUserName("xxxxx");

//        account = accountService.save(account);
//        Assert.isTrue(account.getUserName().equals("User001X"));

        Page<Consume> page =  consumeService.pageListByTenantAndDate("40288aca574060400157406339080002",2016,7,1,1);
        Consume consume = page.getResult().get(0);
        System.out.println(consume.getId());
        Assert.notNull(consume);

        consume.setType("哈哈哈哈");
        System.out.println(JSONUtil.objectToJson(consume));

    }


    @Test
    public void test002(){
        Assert.notNull(jdbcTemplate);
        String sql = "select * from db_lsxy_base.tb_base_account";
        List list = this.jdbcTemplate.queryForList(sql);
        Assert.notNull(list);
        Assert.isTrue(list.size()>0);
    }


}
