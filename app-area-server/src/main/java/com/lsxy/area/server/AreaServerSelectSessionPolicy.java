package com.lsxy.area.server;

import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.session.DefaultSelectSessionPolicy;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.AreaService;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/30.
 * 区域路由
 * 只处理RPCRequest并且只有参数中包含appid参数,并且app已经上线后的消息才能路由到指定的区域
 * 否则报错,也不进入FixQueue
 */
@Component
public class AreaServerSelectSessionPolicy extends DefaultSelectSessionPolicy{

    private static final Logger logger = LoggerFactory.getLogger(AreaServerSelectSessionPolicy.class);

    @Autowired
    private AreaService areaService;

    @Autowired
    private AppService appService;

    public AreaServerSelectSessionPolicy(SessionContext sessionContext) {
        super(sessionContext);
    }

    @Override
    public Session select(RPCMessage message) {
        Session session = null;

        if(message instanceof RPCRequest){
            RPCRequest request = (RPCRequest) message;
            String appid = (String) request.getParameter("appid");
            if(StringUtil.isEmpty(appid)){
                //根据应用所在的区域,找到对应的区域
                App app = appService.findById(appid);
                if(app.getStatus().equals(App.STATUS_ONLINE)){
                    String areaid = app.getArea().getId();
                    session = getRightSessionInArea(areaid);
                }else{
                    
                }
            }else{
                //如果不存在appid表示非应用请求指令,按照默认的负载均衡算法实现路由
                session = super.select(message);
            }
        }else{
            session = super.select(message);
        }
        return session;
    }

    /**
     * 根据区域标识获取所有该区域的区域代理节点,并随机获取一个节点返回
     * @param areaid  区域标识  与数据库id字段同步
     * @return
     */
    private Session getRightSessionInArea(String areaid) {
        Session result = null;
        ServerSessionContext serverSessionContext = (ServerSessionContext) sessionContext;
        ListOrderedMap sessions = serverSessionContext.getSessionsByArea(areaid);
        if(sessions.size()>0){
            //统一个区域随机一个节点
            int idx = RandomUtils.nextInt(1,sessions.size());
            result = (Session) sessions.getValue(idx);
        }
        return result;
    }
}
