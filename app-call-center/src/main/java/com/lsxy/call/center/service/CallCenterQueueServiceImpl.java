package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.call.center.dao.CallCenterQueueDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liuws on 2016/11/14.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class CallCenterQueueServiceImpl extends AbstractService<CallCenterQueue> implements CallCenterQueueService {

    private static final Logger logger = LoggerFactory.getLogger(CallCenterQueueServiceImpl.class);

    @Autowired
    private CallCenterQueueDao callCenterQueueDao;

    @Autowired
    private EntityManager em;

    @Override
    public BaseDaoInterface<CallCenterQueue, Serializable> getDao() {
        return callCenterQueueDao;
    }

    private String getValue(Object obj,Class target){
        if(obj == null){
            return "null";
        }
        if(target.equals(String.class)){
            if(obj instanceof Date){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return "'" + sdf.format((Date)obj)+ "'";
            }
            return "'"+obj.toString()+"'";
        }
        if(target.equals(Long.class)){
            return obj.toString();
        }
        if(target.equals(Integer.class)){
            return obj.toString();
        }
        return "null";
    }

    @Override
    public Iterable<CallCenterQueue> save(Iterable<CallCenterQueue> qs) {
        StringBuilder sql = new StringBuilder("INSERT INTO `db_lsxy_bi_yunhuni`.`tb_bi_call_center_queue` VALUES ");
        List<String> rows = new ArrayList<>();
        Iterator<CallCenterQueue> iterator = qs.iterator();
        while (iterator.hasNext()) {
            CallCenterQueue queue = iterator.next();
            String[] colums = new String[]{
                    getValue(queue.getId(),String.class),
                    getValue(queue.getTenantId(),String.class),
                    getValue(queue.getAppId(),String.class),
                    getValue(queue.getType(),String.class),
                    getValue(queue.getRelevanceId(),String.class),
                    getValue(queue.getStartTime(),String.class),
                    getValue(queue.getInviteTime(),String.class),
                    getValue(queue.getToManualTime(),Long.class),
                    getValue(queue.getDialTime(),String.class),
                    getValue(queue.getEndTime(),String.class),
                    getValue(queue.getResult(),String.class),
                    getValue(queue.getNum(),String.class),
                    getValue(queue.getAgent(),String.class),
                    getValue(queue.getOriginCallId(),String.class),
                    getValue(queue.getAgentCallId(),String.class),
                    getValue(queue.getConversation(),String.class),
                    getValue(queue.getCondition(),String.class),
                    getValue(queue.getEnqueue(),String.class),
                    getValue(queue.getCreateTime(),String.class),
                    getValue(queue.getLastTime(),String.class),
                    queue.getDeleted()?"1":"0",
                    getValue(queue.getDeleteTime(),String.class),
                    getValue(queue.getSortNo(),String.class),
                    getValue(queue.getVersion(),Integer.class),
                    "''"
            };
            StringBuilder row = new StringBuilder("(");
            row.append(StringUtils.join(colums,","));
            row.append(")");
            rows.add(row.toString());
        }
        sql.append(StringUtils.join(rows,","));
        em.createNativeQuery(sql.toString()).executeUpdate();
        return null;
    }
}
