package com.lsxy.app.backend.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by zhangxb on 2016/7/29.
 */
@Component
public class HourStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(HourStatisticsTask.class);
    @Scheduled(cron="0 0 0/1 * * ?")
    public void sout(){
        System.out.println("小时统计任务开启，当前时间" + System.currentTimeMillis());

        //租户 应用 运营商 地区 业务类型 api调用次数

        //租户 应用 运营商 地区 业务类型 消费类型 消费金额增量/总量

        //租户 应用 运营商 地区 业务类型 应用上线个数增量/总量 应用未上线个数增量/总量 应用总个数增量/总量

        //租户 应用 运营商 地区 业务类型 充值次数增量/总量 充值额度增量/总量

        //租户 应用 运营商 地区 业务类型 会话时长增量/总量 会话数量接通个数增量/总量 会话数量未接通个数增量/总量 会话数量总和增量/总量
    }
    private  void api(){
        logger.info("开始api指标小时统计----");
    }
    private void consume(){
        logger.info("开始消费指标小时统计----");
    }
    private void app(){
        logger.info("开始应用指标小时统计----");
    }
    private void recharge(){
        logger.info("开始充值指标小时统计----");
    }
    private void session(){
        logger.info("开始会话指标小时统计----");
    }
}
