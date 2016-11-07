package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.AgentSkillService;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.call.center.dao.AgentSkillDao;
import com.lsxy.call.center.dao.AppExtensionDao;
import com.lsxy.call.center.dao.CallCenterAgentDao;
import com.lsxy.call.center.utils.EnqueueSQLUtil;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangxb on 2016/10/21.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class CallCenterAgentServiceImpl extends AbstractService<CallCenterAgent> implements CallCenterAgentService {

    private static final Logger logger = LoggerFactory.getLogger(CallCenterAgentServiceImpl.class);

    @Autowired
    private CallCenterAgentDao callCenterAgentDao;

    @Autowired
    private AgentSkillService agentSkillService;

    @Autowired
    private AppExtensionService appExtensionService;

    @Autowired
    private AppExtensionDao appExtensionDao;

    @Autowired
    private AgentSkillDao agentSkillDao;

    @Reference(lazy = true,check = false,timeout = 3000)
    private DeQueueService deQueueService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MQService mqService;

    @Override
    public BaseDaoInterface<CallCenterAgent, Serializable> getDao() {
        return callCenterAgentDao;
    }

    //登陆
    @Override
    public String login(String tenantId,String appId,CallCenterAgent callCenterAgent,List<AppExtension> extentions,List<AgentSkill> skills){
        if(StringUtil.isEmpty(tenantId)){
            throw new NullPointerException();
        }
        if(StringUtil.isEmpty(appId)){
            throw new NullPointerException();
        }

        CallCenterAgent agent = new CallCenterAgent();
        agent.setTenantId(tenantId);
        agent.setAppId(appId);
        agent.setAgentNo(callCenterAgent.getAgentNo());
        agent.setAgentNum(callCenterAgent.getAgentNum());

        String agentId = this.save(agent).getId();
        if(extentions!=null && extentions.size()>0){
            for (AppExtension extension : extentions) {
                //TODO 设置坐席的分机
            }
        }
        if(skills!=null && skills.size()>0){
            for (AgentSkill obj : skills) {
                AgentSkill skill = new AgentSkill();
                try {
                    BeanUtils.copyProperties(skill,obj);
                } catch (Throwable e) {
                    throw new IllegalArgumentException(e);
                }
                skill.setTenantId(tenantId);
                skill.setAppId(appId);
                skill.setAgent(agentId);
                agentSkillService.save(skill);
            }
        }
        return agentId;
    }

    //注销
    @Override
    public boolean logout(String agentId){
        agentSkillDao.deleteByAgent(agentId);
        //TODO 删除坐席的分机列表
        try {
            this.delete(agentId);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        return true;
    }

    //设置坐席状态
    public boolean setState(String id,String state){
        CallCenterAgent agent = this.findById(id);
        if(agent == null){
            return true;
        }
        //TODO 设置坐席的状态
        this.save(agent);
        return true;
    }
    //技能迁入迁出
    @Override
    public boolean checkInSkill(String agent,String skillName,Integer active){
        agentSkillDao.updateActiveByAgent(active,agent,skillName);
        return true;
    }
    //技能追加
    @Override
    public boolean appendSkill(String tenantId,String appId,String agent,String name,Integer level,Integer active){
        AgentSkill skill = new AgentSkill();
        skill.setTenantId(tenantId);
        skill.setAppId(appId);
        skill.setAgent(agent);
        skill.setName(name);
        skill.setLevel(level);
        skill.setActive(active);
        agentSkillService.save(skill);
        return true;
    }

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

    String skill_regex="(has|get)\\(\"(.+?)\"\\)";
    Pattern p  =Pattern.compile(skill_regex);

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

    public boolean getWhere(String enqueue , Map<String,Object> params){
        if(StringUtil.isBlank(enqueue)){
            return false;
        }
        EnQueue enQueue = JSONUtil2.fromJson(enqueue,EnQueue.class);
        String ex = enQueue.getFilter().getCondition().getWhere();
        if(StringUtil.isBlank(ex)){//没有条件,就是所有都满足
            return true;
        }
        Matcher m =  p.matcher(ex);
        while(m.find()){
            String key = m.group(1);
            String val =  m.group(2);
            String varName = "var_" + val.hashCode();
            String rel = varName;
            if(key.equals("has")){
                rel = rel + ">0";
            }
            ex = ex.replaceAll(key + "\\(\"(.+?)\"\\)",rel);
        }
        return EnqueueSQLUtil.execWhereExpression(ex,params);
    }

    public long getSort(String enqueue , Map<String,Object> params){
        if(StringUtil.isBlank(enqueue)){
            return Integer.MIN_VALUE;
        }
        EnQueue enQueue = JSONUtil2.fromJson(enqueue,EnQueue.class);
        String ex = enQueue.getFilter().getCondition().getSort();
        if(StringUtil.isBlank(ex)){//没有排序表达式
            return Integer.MAX_VALUE;
        }
        Matcher m =  p.matcher(ex);
        while(m.find()){
            String key = m.group(1);
            String val =  m.group(2);
            String varName = "var_" + val.hashCode();
            String rel = varName;
            if(key.equals("has")){
                rel = rel + ">0";
            }
            ex = ex.replaceAll(key + "\\(\"(.+?)\"\\)",rel);
        }
        return EnqueueSQLUtil.execSortExpression(ex,params);
    }
    public void dequeue(String agent){
    }
}
