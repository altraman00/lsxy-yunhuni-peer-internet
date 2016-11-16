package com.lsxy.area.server.service.callcenter;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.call.center.api.model.EnQueueResult;
import com.lsxy.call.center.api.service.DeQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/18.
 */
@Service
@Component
public class DeQueueServiceImpl implements DeQueueService {

    private static final Logger logger = LoggerFactory.getLogger(DeQueueServiceImpl.class);


    @Override
    public void success(String tenantId, String appId, String callId,String queueId, EnQueueResult result) {
        logger.info("=====================+success");
    }

    @Override
    public void timeout(String tenantId, String appId, String callId) {
        logger.info("=====================+timeout");
    }

    @Override
    public void fail(String tenantId, String appId, String callId, String reason) {
        logger.info("=====================+fail");
    }
}
