package com.lsxy.app.mc.controllers;

import com.lsxy.app.mc.vo.ServerVO;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.JSONUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

/**
 * Created by tandy on 16/11/19.
 */
@Controller
public class ServerController extends AdminController{

    @Autowired
    private RedisCacheService cacheService;

    @RequestMapping("/servers")
    public String servers(Model model){

        List<ServerVO> servers = getAllServerList();
//        servers.add(new ServerVO("用户控制台","app-portal-01","p01","192.168.10.2","8080","OK"));
//        servers.add(new ServerVO("用户控制台","app-portal-01","p01","192.168.10.2","8080","FAILED"));
        model.addAttribute("servers",servers);





        return "servers";
    }

    /**
     * 从配置文件中获取所有服务器并根据服务器配置获取服务器当前状态并返回
     * @return
     */
    private List<ServerVO> getAllServerList() {
        String serverListJson = SystemConfig.getProperty("app.mc.serverlist","[]");
        List<ServerVO> result = JSONUtil2.jsonToList(serverListJson,ServerVO.class);
        Set<String> serversCache = cacheService.keys("monitor_*");

        for(ServerVO server:result){
            String key = "monitor_"+server.getAppName()+"_"+server.getServerHost()+"_";
            if(serversCache.contains(key)){
                server.setStatus(ServerVO.STATUS_OK);
            }else{
                server.setStatus(ServerVO.STATUS_FAILED);
            }
        }
        return result;
    }


}
