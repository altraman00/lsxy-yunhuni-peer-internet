package com.lsxy.area.agent.cti;

import com.lsxy.app.area.cti.Commander;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by tandy on 16/8/5.
 * cti 客户端  环境 用来管理多个客户端连接
 */
@Component
public class CTIClientContext {

    public static final String KEY_CTI_CLUSTER = "hesong:ipsc:cluster:nodeid";
    private RedisCacheService cacheService;

    private Set<String> servers = new HashSet<>();

    private Map<String,CTINode> nodes = new TreeMap<>();


    public void init(){
        if(logger.isDebugEnabled()){
            logger.debug("初始化CTI配置 From Redis");
        }
        loadConfig();
    }


    /**
     * 加载CTI配置
     */
    public void loadConfig(){
        Set<String> ipscNodes = cacheService.smembers(KEY_CTI_CLUSTER);
        ipscNodes.forEach((snode)->{
            Object obj = cacheService.hget(KEY_CTI_CLUSTER + ":"+snode,"ip");
            String sServer = obj.toString();
            servers.add(sServer.substring(0,sServer.indexOf("-")));
            nodes.put(snode,new CTINode(snode,sServer));
        });
    }


    private static final Logger logger = LoggerFactory.getLogger(CTIClientContext.class);
    public HashMap<String,Commander> clients = new HashMap<>();


    /**
     * 注册commander对象到环境中
     * @param serverIp
     * @param commander
     */
    public void registerCommander(String serverIp, Commander commander) {
        clients.put(serverIp, commander);
        for(String key:nodes.keySet()){
            CTINode node = nodes.get(key);
            if(node.getUnitId().equals(commander.getUnitId()+"")){
                node.setCtiCommander(commander);
            }
        }
    }
//
//    public CTINode getAvalibleNode() {
//        return getAvalibleNode(null);
//    }
    /**
     * 获取一个有效的CTI客户端连接对象进行操作
     * 需要考虑:CTI负载情况  会话相关(会议成员需要被分配到同一个CTI服务)
     * 根据参考ID获取Node ，如果参考id为空，就采用负载最少的节点返回
     * @param referenceResId  参考资源id 资源ID可以解析出nodeid
     *                        eg:1.0.0-sys.call-11000016035539044
     * @return
     */
    public CTINode getAvalibleNode(String referenceResId) {
        CTINode node = null;
        if(StringUtil.isNotEmpty(referenceResId)){
            String nodeId = referenceResId.substring(0,referenceResId.indexOf("-"));
            node = nodes.get(nodeId);
            logger.error("指定的参考资源ID未找到对应的CTI节点："+referenceResId);
            return node;
        }
        //如果资源id为空，就查询负载最小的节点
        try {
            for (String key:nodes.keySet()) {
                CTINode xnode = nodes.get(key);
                if(!xnode.isReady()){
                    continue;
                }
                if(node == null){
                    node = xnode;
                }else{
                    if(xnode.getLoadValue() < node.getLoadValue()){
                        node = xnode;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("查找有效CTINODE时出现异常：",ex);
        }

        if (node == null) {
            logger.error("没有找到一个有效的CTI客户端");

        }else {
            if (logger.isDebugEnabled()) {
                logger.debug("找到有效的CTINode:" + node);
            }
        }
        return node;
    }

    public Map<String, CTINode> sortMapByValue(Map<String, CTINode> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        TreeMap<String,CTINode> sortMap = new TreeMap<>(new CTINodeCompareator(map));
        sortMap.putAll(map);
        return sortMap;
    }



    public boolean isNotExist(String serverIp) {
        return this.clients.containsKey(serverIp);
    }

    public void remove(String ip) {
        clients.remove(ip);
    }

    public Set<String> getCTIServers() {
        return servers;
    }

    /**
     * 更新负载数据
     * @param unitId
     * @param pid
     * @param loadData
     * callin.count=116347,
     * callout.count=116797,
     * sip.in.total.num=1000,
     * ch.total.num=2000,
     * dsp.used.num=107,
     * sip.callout.num=405,
     * sip.callin.num=405,
     * loadlevel=0,
     * callin.num=405,
     * sip.out.total.num=1000,
     * callout.num=405
     */
    public void updateNodeLoadData(String unitId, String pid, Map<String, Integer> loadData) {
        String nodeKey = "1."+unitId+"."+pid;
        CTINode node = nodes.get(nodeKey);

        node.setCinCount(loadData.get("callin.count="));
        node.setCoutCount(loadData.get("callout.count"));
        node.setCinNumber(loadData.get("callin.num"));
        node.setCoutNumber(loadData.get("callout.num"));

        node.setReady(true);
    }

    class CTINodeCompareator implements  Comparator<String>{

        Map<String,CTINode> nodes;

        public CTINodeCompareator(Map<String,CTINode> nodes){
            this.nodes = nodes;
        }

        @Override
        public int compare(String o1, String o2) {
            CTINode n1 = nodes.get(o1);
            CTINode n2 = nodes.get(o2);

            if((n1.getCinNumber() + n1.getCoutNumber()) > (n2.getCinNumber() + n2.getCoutNumber())){
                return 1;
            }else{
                return -1;
            }
        }
    }



    public static void main(String[] args) {
//        CTIClientContext x = new CTIClientContext();
//
//
//        TreeMap<String,CTINode> treeMap = new TreeMap<>();
//
//        CTINode node1 = new CTINode("1.0.1","1");
//        CTINode node2 = new CTINode("2.0.1","1");
//        CTINode node3 = new CTINode("3.0.1","1");
//        CTINode node4 = new CTINode("1.0.1","1");
//        CTINode node5 = new CTINode("2.0.1","1");
//        CTINode node6 = new CTINode("3.0.1","1");
//        node1.setCinNumber(100);
//        node2.setCinNumber(200);
//        node3.setCinNumber(300);
//        node5.setCinNumber(500);
//        node4.setCinNumber(400);
//        node6.setCinNumber(322);
//
//        treeMap.put("3",node1);
//        treeMap.put("2",node3);
//        treeMap.put("1",node2);
//
//        treeMap.put("5",node4);
//        treeMap.put("7",node5);
//        treeMap.put("9",node6);
//        Map<String,CTINode> map1 = x.sortMapByValue(treeMap);
//
//        Iterator<String> it = map1.keySet().iterator();
//        while(it.hasNext()){
//            String key = it.next();
//            CTINode node = map1.get(key);
//            System.out.println(node.getCinNumber());
//        }
        System.out.println("12.202.02-sys.call-11000016035539044".matches("\\d+\\.\\d+\\.\\d+-.*"));
        System.out.println("1.0.0-sys.call-11000016035539044".substring(0,"1.0.0-sys.call-11000016035539044".indexOf("-")));

    }

}
