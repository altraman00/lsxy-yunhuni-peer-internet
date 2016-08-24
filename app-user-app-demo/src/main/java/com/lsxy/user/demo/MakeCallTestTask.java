package com.lsxy.user.demo;

import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.lsxy.framework.web.rest.RestRequest.buildRequest;
import static org.apache.el.util.MessageFactory.get;

/**
 * Created by tandy on 16/8/8.
 */
public class MakeCallTestTask implements  Runnable {

    private int execCount;

    private String uri = "http://api.dev.yunhuni.com/v1/account/1234567/call";
//        return  RestRequest.buildSecurityRequest(token).post(uri, paramsMap, Account.class);

    private static final Logger logger = LoggerFactory.getLogger(MakeCallTestTask.class);
    public MakeCallTestTask(int execCount) {
        this.execCount = execCount;
    }

    @Override
    public void run() {
        logger.info("线程启动执行:{}",Thread.currentThread().getName());

        while(execCount -- >0){
            logger.info("执行CALL");
            RestRequest request = RestRequest.buildRequest();
            RestResponse<String> response = request.get(uri,String.class);
            logger.info("请求结果:" + response);
        }
    }
}
