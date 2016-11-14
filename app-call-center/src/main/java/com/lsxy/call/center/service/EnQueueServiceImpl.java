package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.EnQueueService;

/**
 * Created by liuws on 2016/11/14.
 */
public class EnQueueServiceImpl implements EnQueueService{

    /**
     * 排队,通过 dubbo返回结果给  区域管理器
     * @param tenantId
     * @param appId
     * @param callId 参与排队的callId
     * @param enQueue
     */
    @Override
    public void enqueue(String tenantId, String appId, String callId, EnQueue enQueue){
        /*if(StringUtil.isEmpty(tenantId)){
            return;
        }
        if(StringUtil.isEmpty(appId)){
            return;
        }
        if(enQueue == null){
            return;
        }
        try{
            int timeout = 60;
            String where = "";
            String sort = "";
            if(enQueue.getFilter()!=null && enQueue.getFilter().getCondition()!=null){
                timeout = enQueue.getFilter().getCondition().getTimeout();
                where = enQueue.getFilter().getCondition().getWhere();
                sort = enQueue.getFilter().getCondition().getWhere();
            }
            String sql = EnqueueSQLUtil.genSQL(tenantId,appId,where,sort);
            List<EnQueueResult> results = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<EnQueueResult>(EnQueueResult.class));
            EnQueueResult result = null;
            if(results != null && results.size()>0){
                for (EnQueueResult res: results) {
                    boolean locked = "1".equals(redisTemplate.opsForValue().getAndSet("agent_lock_"+res.getAgent(),"1"));
                    if(!locked){
                        result = res;
                        break;
                    }
                }
            }
            if(result != null){
                deQueueService.success(tenantId,appId,callId,result);
            }else{
                //将排队放入redis
                String key = "enqueue_" +tenantId+"_"+appId + UUIDGenerator.uuid();
                redisTemplate.opsForValue().set(key,"1");
                //排队队列左进右出
                redisTemplate.opsForList().leftPush("enqueue_"+tenantId+"_"+appId, JSONUtil.objectToJson(enQueue));
                mqService.publish(new EnqueueEvent(key,tenantId,appId,callId,timeout));
            }
        }catch (Throwable t){
            logger.error("排队出错",t);
            deQueueService.fail(tenantId,appId,callId,t.getMessage());
        }*/
    }

    /**
     * 坐席找排队
     * @param tenantId
     * @param appId
     * @param agentId
     */
    public void enqueue(String tenantId, String appId, String agentId){
        /*CallCenterAgent agent = this.findById(agentId);
        if(agent == null){
            return;
        }
        List<AgentSkill> skills = agentSkillDao.findByTenantIdAndAppIdAndAgentAndActive(tenantId,appId,agentId,1);
        Map<String,Object> params = new HashMap<>();
        if(skills!=null && skills.size()>0){
            for(AgentSkill skill : skills){
                String varName = "var_" + skill.getName().hashCode();
                params.put(varName, skill.getLevel() == null ?0 : skill.getLevel());
            }
        }
        String enqueue = redisTemplate.opsForList().rightPop("enqueue_"+tenantId+"_"+appId);
        if(getWhere(enqueue,params)){
            getSort(enqueue,params);
        }*/
    }

}
