package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.*;
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
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.callcenter.EnqueueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

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
    private RedisTemplate<String,String> redisTemplate;

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
    public String login(String tenantId,String appId,CallCenterAgent callCenterAgent){
        if(StringUtil.isEmpty(tenantId)){
            throw new NullPointerException();
        }
        if(StringUtil.isEmpty(appId)){
            throw new NullPointerException();
        }

        CallCenterAgent agent = new CallCenterAgent();
        agent.setTenantId(tenantId);
        agent.setAppId(appId);
        agent.setName(callCenterAgent.getName());
        agent.setState(callCenterAgent.getState());
        if(StringUtil.isBlank(agent.getState())){
            agent.setState(CallCenterAgent.STATE_DEFAULT);
        }
        String agentId = this.save(agent).getId();
        List<AppExtension>  extensions = callCenterAgent.getExtentions();
        if(extensions!=null && extensions.size()>0){
            for (AppExtension extension : extensions) {
                appExtensionService.updateAgent(extension.getId(),agentId,extension.getEnabled());
            }
        }
        List<AgentSkill> skills = callCenterAgent.getSkills();
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
        appExtensionDao.updateByAgent(AppExtension.UNENABLED,agentId);
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
        agent.setState(state);
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
        if(StringUtil.isEmpty(tenantId)){
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
        }
    }

    public void enqueue(String tenantId, String appId, String agent){


    }
    public void dequeue(String agent){
    }

    /*public static void main(String[] args) {
        String skill_regex="(has|get)\\(\"(.+?)\"\\)";
        Pattern p  =Pattern.compile(skill_regex);
        String ex = "get(\"haha0\") + get(\"haha1\")";
        Matcher m =  p.matcher(ex);
        List<String> vars = new ArrayList<>();
        while(m.find()){
            String key = m.group(1);
            String val =  m.group(2);
            String varName = "var_" + val.hashCode();
            String rel = varName;
            if(key.equals("has")){
                rel = rel + ">0";
            }
            ex = ex.replaceAll(key + "\\(\"(.+?)\"\\)",rel);
            vars.add(varName);
        }
        Map<String,Object> params = new HashMap<>();
        for(String var : vars){
            params.put(var, new Random().nextInt(100));
        }
        System.out.println(EnqueueSQLUtil.execExpression(ex,params).getValue());
    }*/
}
