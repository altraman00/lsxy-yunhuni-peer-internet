package com.oneyun.sapi;

import com.oneyun.sapi.utils.JsonUtils;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by liups on 2016/12/5.
 */
public class AgentAndExtension {
    public static SapiSdk sapiSdk = new SapiSdk("http://api.yunhuni.cn/v1/account/0c2794d1e1ac22a802f697be8aa70bc8/","0c2794d1e1ac22a802f697be8aa70bc8",
            "b7496195f464a00fa6b3fca72f672928");
    public static String appId = "8a2bc67258cdafa80158cdd1b0ac0001";


    public List<String> getExtIds() throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        List<String> extIds = new ArrayList<>();
        String result = sapiSdk.CallcenterExtensionGetPage(appId, 1, 500);
        Map map = JsonUtils.toMap(result);
        Map page = (Map) map.get("data");
        List<Map> list = (List<Map>) page.get("result");
        list.parallelStream().forEach(ext -> extIds.add(ext.get("id").toString()));
        return extIds;
    }

    @Test
    public void creatExtension() throws InterruptedException {
        List<String> extIds = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> results = new ArrayList<>();
        for(int i=0;i<500;i++){
            String user = i + "";
            Thread.sleep(20);
            results.add(executorService.submit(() -> sapiSdk.CallcenterExtensionNew(appId, 1, user, "123456", null, null)));
        }
        for(Future f : results){
            try {
                String result = (String) f.get();
                System.out.println(result);
                Map map = JsonUtils.toMap(result);
                Map data = (Map) map.get("data");
                extIds.add((String) data.get("id"));
            }catch (Throwable t){

            }
        }
    }


    @Test
    public void deleteExts() throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        List<String> extIds = getExtIds();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> results = new ArrayList<>();
        //删除座席
        extIds.parallelStream().forEach(extId -> results.add(executorService.submit(() -> sapiSdk.CallcenterExtensionDelete(appId,extId))));

        for(Future f : results){
            try {
                String result = (String) f.get();
                System.out.println(result);
            }catch (Throwable t){

            }
        }
    }

    @Test
    public void createAgent() throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException, InterruptedException {
        List<String> extIds = searchUsableExtensionIds();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> results = new ArrayList<>();
        //建立座席
        for(int i=0;i<150;i++){
            Thread.sleep(20);
            String extId = extIds.get(i);
            List<AgentSkill> skills = new ArrayList<>();
            AgentSkill agentSkill1 = new AgentSkill("投诉", 60, true);
            skills.add(agentSkill1);
            AgentSkill agentSkill2 = new AgentSkill("建议", 60, true);
            skills.add(agentSkill2);
            String user = i + "";
            results.add(executorService.submit(() -> sapiSdk.CallcenterAgentLogin(appId, user,  "8a2a6a4a58cdd3fe0158cdfce2de0005",  user,  "idle", skills, extId)));
        }

        for(Future f : results){
            try {
                String result = (String) f.get();
                System.out.println(result);
            }catch (Throwable t){

            }
        }
    }

    @Test
    public void deleteAgent() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> results = new ArrayList<>();
        //删除座席
        for(int i=0;i<500;i++){
            Thread.sleep(20);
            String user = i + "";
            results.add(executorService.submit(() -> sapiSdk.CallcenterAgentLogout(appId,user,true)));
        }

        for(Future f : results){
            try {
                String result = (String) f.get();
                System.out.println(result);
            }catch (Throwable t){

            }
        }
    }

    @Test
    public void searchAgent() throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        System.out.println(" * ━━━━━━神兽出没━━━━━━ \n" +
                " * 　　　┏┓　　　┏┓ \n" +
                " * 　　┏┛┻━━━┛┻┓ \n" +
                " * 　　┃　　　　　　　┃ \n" +
                " * 　　┃　　　━　　　┃ \n" +
                " * 　　┃　┳┛　┗┳　┃ \n" +
                " * 　　┃　　　　　　　┃ \n" +
                " * 　　┃　　　┻　　　┃ \n" +
                " * 　　┃　　　　　　　┃ \n" +
                " * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting \n" +
                " * 　　　　┃　　　┃    神兽保佑,代码无bug \n" +
                " * 　　　　┃　　　┃ \n" +
                " * 　　　　┃　　　┗━━━┓ \n" +
                " * 　　　　┃　　　　　　　┣┓ \n" +
                " * 　　　　┃　　　　　　　┏┛ \n" +
                " * 　　　　┗┓┓┏━┳┓┏┛ \n" +
                " * 　　　　　┃┫┫　┃┫┫ \n" +
                " * 　　　　　┗┻┛　┗┻┛ \n" +
                " * \n" +
                " * ━━━━━━感觉萌萌哒━━━━━━ ");
        String result = sapiSdk.CallcenterAgentGetPage(appId, 1, 500);
        Map map = JsonUtils.toMap(result);
        Map date = (Map) map.get("data");
        List<Map> agents = (List<Map>) date.get("result");
        System.out.println(agents.size());
        agents.parallelStream().filter(agent -> !"idle".equals(agent.get("state"))).forEach(agent -> {System.out.println(agent.get("name") + ":" + agent.get("state") + "," +  agent.get("extension"));
        });
    }

    @Test
    public void searchExtension() throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        List<String> extIds = getExtIds();
        String result = sapiSdk.extensionListTest(appId, 1, 500);
        Map map = JsonUtils.toMap(result);
        List<Map> list = (List<Map>) map.get("data");
        List<Map> collect = list.parallelStream().filter(ext -> {
            Set<String> set = ext.keySet();
            String key = null;
            for (String k : set) {
                key = k;
                break;
            }
            Collection values = ext.values();
            return extIds.contains(key) && values.contains(true);
        }).collect(Collectors.toList());
        System.out.println(collect.size());

//        collect.parallelStream().forEach(ext -> {
//                    System.out.println(ext.keySet());
//                });
    }


    @Test
    public void printSearchUsableExtensionIds() throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        List<String> strings = searchUsableExtensionIds();
        System.out.println(strings.size());
        System.out.println(strings);
    }

    public List<String> searchUsableExtensionIds() throws KeyManagementException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        String result = sapiSdk.extensionListTest(appId, 1, 500);
        Map map = JsonUtils.toMap(result);
        List<Map> list = (List<Map>) map.get("data");
        List<String> usableExtIds = new ArrayList<>();
        list.parallelStream().forEach(ext -> {
            Set<String> set = ext.keySet();
            String key = null;
            for (String k : set) {
                key = k;
                break;
            }
            Collection values = ext.values();
            if(values.contains(true)){
                usableExtIds.add(key);
            }
        });
        return usableExtIds;
    }

}
