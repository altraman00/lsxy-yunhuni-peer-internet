package com.lsxy.call.center.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 管理坐席的条件的zset
 * key callcenter.acs__agentid
 *     item ==>  cid:priority
 */
@Component
public class ACs {
    private static final Logger logger = LoggerFactory.getLogger(ACs.class);

    private static final String PREFIXED_KEY = "callcenter.acs_";

    private String getKey(String agentId){
        return PREFIXED_KEY + agentId;
    }
}
