package com.lsxy.msg;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by liups on 2016/6/28.
 * 将当前类路径下的所有jpa的类DAO Repository暴露
 * 暴露当前JAR  包中的所有服务组件
 * 使用方法
 */
@EnableJpaRepositories
@ComponentScan
public class MsgServiceConfig {
}
