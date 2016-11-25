package com.lsxy.app.mc.controllers;

import com.lsxy.app.mc.vo.AreaNodeVO;
import com.lsxy.app.mc.vo.AreaServerHostVO;
import com.lsxy.app.mc.vo.AreaServerVO;
import com.lsxy.app.mc.vo.ServerVO;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

import static com.lsxy.framework.core.utils.JSONUtil2.jsonToList;

/**
 * Created by tandy on 16/11/19.
 */
@Controller
public class ServerController extends AdminController{

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);
    
    @Autowired
    private RedisCacheService cacheService;

    @RequestMapping("/servers")
    public String servers(Model model){

        //查询所有服务器节点运行情况
        List<ServerVO> servers = getAllServerList();
        model.addAttribute("servers",servers);

        //查询区域服务连接情况
        List<AreaServerVO> areaServers = getAllAreaServers();
        model.addAttribute("areaServers",areaServers);

        return "servers";
    }

    /**
     * 从配置文件中获取所有服务器并根据服务器配置获取服务器当前状态并返回
     * @return
     */
    private List<ServerVO> getAllServerList() {
        String serverListJson = SystemConfig.getProperty("app.mc.serverlist","[]");
        List<ServerVO> result = jsonToList(serverListJson,ServerVO.class);
        Set<String> serversCache = cacheService.keys("monitor_*");
        if(logger.isDebugEnabled()){
            logger.debug("search monitor_* from redis and result is : {}" ,serversCache);
        }

        for(ServerVO server:result){
            String key = "monitor_"+server.getAppName()+"_"+server.getServerHost()+"_";
            if(logger.isDebugEnabled()){
                logger.debug("find server connection status, key is :{}",key);
            }
            if(serversCache.contains(key)){
                server.setStatus(ServerVO.STATUS_OK);
            }else{
                server.setStatus(ServerVO.STATUS_FAILED);
            }
        }
        return result;
    }


    private List<AreaServerVO> getAllAreaServers(){
        String areaServerListJson = SystemConfig.getProperty("app.mc.areaservers","[]");
        if(logger.isDebugEnabled()){
            logger.debug("get all area servers using json:{}" , areaServerListJson);
        }

        List<AreaServerVO> areaServers = JSONUtil2.jsonToList(areaServerListJson,AreaServerVO.class);
        for(AreaServerVO areaServer:areaServers){
            for(AreaServerHostVO areaServerHost :areaServer.getHosts()){
                String cacheKey = "monitor_area.server_"+areaServerHost.getHostName()+"_sessionContext";
                String sessionContextJson = cacheService.get(cacheKey);

                if(logger.isDebugEnabled()){
                    logger.debug("cachekey {} result is : {}",cacheKey,sessionContextJson);
                }

                if(StringUtil.isNotEmpty(sessionContextJson)){
                    for(AreaNodeVO areaNode:areaServerHost.getNodes()){
                        if(sessionContextJson.indexOf(areaNode.getNodeId())>0){
                            areaNode.setStatus(AreaNodeVO.STATUS_OK);
                        }
                    }
                }
            }
        }
        return areaServers;
    }

//    public static void main(String[] args) {
//        String json = "[{\"areaId\":\"area001\",\"areaName\":\"area001\"," +
//                "    \"hosts\":[" +
//                "    {\"hostName\":\"localhost\",\"nodes\":[" +
//                "        {\"nodeId\":\"area001-1\",\"host\":\"localhost\"}," +
//                "        {\"nodeId\":\"area001-1\",\"host\":\"localhost\"}" +
//                "]" +
//                "            }]" +
//                "        }]" +
//                "   }";
//        List<AreaServerVO> result = JSONUtil2.jsonToList(json, AreaServerVO.class);
//        result = JSONUtil2.jsonToList(json, AreaServerVO.class);
//        System.out.println(result.size());
//    }

}
