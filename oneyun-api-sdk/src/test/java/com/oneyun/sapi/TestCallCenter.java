package com.oneyun.sapi;

import com.oneyun.sapi.config.SapiConstants;
import com.oneyun.sapi.utils.HttpClientUtils;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 呼叫中心测试
 * Created by zhangxb on 2017/1/17.
 */
public class TestCallCenter extends BaseTest{
    private static String appId =  "8a2a6a4a59abba000159abdf29d10000";
    public static void CallcenterExtensionNew(String appId,Integer type,String user,String password,String ipaddr,String telnum )  {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("user", user);
        params.put("password", password);
        params.put("ipaddr", ipaddr);
        params.put("telnum", telnum);
        logger.info("[创建分机]参数：" + params);
        try {
            String result = HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_EXTENSION), sapiSdk.getSecretKey(), sapiSdk.getCertId(), appId, params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test1(){
        Integer type = 1;
        String user = "123456";
        String password = "123456";
        String ipaddr = null;
        String telnum = null;
        logger.info("[开始][type设置1]");
        CallcenterExtensionNew(  appId, type, user, password, ipaddr, telnum );
        logger.info("[结束][type设置1]");
    }
    @Test
    public void test2(){
        Integer type = 4;
        String user = "123456";
        String password = "123456";
        String ipaddr = null;
        String telnum = null;
        logger.info("[开始][type设置其他枚举值]");
        CallcenterExtensionNew(  appId, type, user, password, ipaddr, telnum );
        logger.info("[结束][type设置其他枚举值]");
    }
    @Test
    public void test3(){
        Integer type = 1;
        String user = "12345";
        String password = "123456";
        String ipaddr = null;
        String telnum = null;
        logger.info("[开始][user设置5个数字]");
        CallcenterExtensionNew(  appId, type, user, password, ipaddr, telnum );
        logger.info("[结束][user设置5个数字]");
    }
    @Test
    public void test4(){
        Integer type = 1;
        String user = "123456";
        String password = "123456";
        String ipaddr = null;
        String telnum = null;
        logger.info("[开始][user设置6个数字]");
        CallcenterExtensionNew(  appId, type, user, password, ipaddr, telnum );
        logger.info("[结束][user设置6个数字]");
    }
    @Test
    public void test5(){
        Integer type = 1;
        String user = "1234567890123";
        String password = "123456";
        String ipaddr = null;
        String telnum = null;
        logger.info("[开始][user设置13个数字]");
        CallcenterExtensionNew(  appId, type, user, password, ipaddr, telnum );
        logger.info("[结束][user设置13个数字]");
    }
    @Test
    public void test6(){
        Integer type = 1;
        String user = "1234569";
        String password = "12345";
        String ipaddr = null;
        String telnum = null;
        logger.info("[开始][password设置5个字符]");
        CallcenterExtensionNew(  appId, type, user, password, ipaddr, telnum );
        logger.info("[结束][password设置5个字符]");
    }
    @Test
    public void test7(){
        Integer type = 1;
        String user = "1234570";
        String password = "123456";
        String ipaddr = null;
        String telnum = null;
        logger.info("[开始][password设置6个字符]");
        CallcenterExtensionNew(  appId, type, user, password, ipaddr, telnum );
        logger.info("[结束][password设置6个字符]");
    }
    @Test
    public void test8(){
        Integer type = 1;
        String user = "1234591";
        String password = "1234567890123";
        String ipaddr = null;
        String telnum = null;
        logger.info("[开始][password设置6个字符]");
        CallcenterExtensionNew(  appId, type, user, password, ipaddr, telnum );
        logger.info("[结束][password设置6个字符]");
    }

    /**
     *  根据分机id获取分机
     * @param appId 应用id
     * @param extensionId 分机id
     * @return
     */
    public  static void  CallcenterExtensionByExtensionId(String appId,String  extensionId) {
        Map<String, Object> params = new HashMap<String, Object>();
        logger.info("[查询分机]参数：" + params);
        try {
            String result = HttpClientUtils.doGet(sapiSdk.getUrl(SapiConstants.CALLCENTER_EXTENSION + "/" + extensionId), sapiSdk.getSecretKey(), sapiSdk.getCertId(), appId, params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    /**
     *  获取分页分机
     * @param appId 应用id
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    public static void  CallcenterExtensionGetPage(String appId,String pageNo,String pageSize){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        logger.info("[查询分机]参数：" + params);
        try {
            String result = HttpClientUtils.doGet(sapiSdk.getUrl(SapiConstants.CALLCENTER_EXTENSION),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test9(){
        String pageNo = null;
        String pageSize = null;
        logger.info("[开始][pageNo为空]");
        CallcenterExtensionGetPage( appId, pageNo, pageSize);
        logger.info("[结束][pageNo为空]");
    }
    @Test
    public void test10(){
        String pageNo = "3";
        String pageSize = null;
        logger.info("[开始][pageNo设置3]");
        CallcenterExtensionGetPage( appId, pageNo, pageSize);
        logger.info("[结束][pageNo设置3]");
    }
    @Test
    public void test11(){
        String pageNo = "1";
        String pageSize = null;
        logger.info("[开始][pageSize为空]");
        CallcenterExtensionGetPage( appId, pageNo, pageSize);
        logger.info("[结束][pageSize为空]");
    }
    @Test
    public void test12(){
        String pageNo = "1";
        String pageSize = "5";
        logger.info("[开始][pageSize设置5]");
        CallcenterExtensionGetPage( appId, pageNo, pageSize);
        logger.info("[结束][pageSize设置5]");
    }

    public static void  CallcenterChannelNew(String appId, String maxAgent,String maxSkill,String maxCondition,String maxQueue,String remark){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("max_agent",maxAgent);
        params.put("max_skill",maxSkill);
        params.put("max_condition",maxCondition);
        params.put("max_queue",maxQueue);
        params.put("remark",remark);
        logger.info("[创建通道]参数：" + params);
        try {
            String result = HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_CHANNEL),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test13(){
        String maxAgent = "5";
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_agent设置]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_agent设置]");
        //8a2bc67259abba740159af4aea3a0006
    }
    @Test
    public void test14(){
        String maxAgent = "-10";
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_agent设置-10]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_agent设置-10]");
        //8a2bc67259abba740159af4aea3a0006
    }
    @Test
    public void test15(){
        String maxAgent = "5.5";
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_agent设置5.5]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_agent设置5.5]");
        //8a2bc67259abba740159af4aea3a0006
    }
    @Test
    public void test16(){
        String maxAgent = null;
        String maxSkill = "10";
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_skill设置]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_skill设置]");
        //8a2bc67259abba740159af53da400009
    }
    @Test
    public void test17(){
        String maxAgent = null;
        String maxSkill = "-10";
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_skill设置-10]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_skill设置-10]");
        //8a2bc67259abba740159af53da400009
    }
    @Test
    public void test18(){
        String maxAgent = null;
        String maxSkill = "5.5";
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_skill设置5.5]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_skill设置5.5]");
        //8a2bc67259abba740159af53da400009
    }
    @Test
    public void test19(){
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = "5";
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_condition设置]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_condition设置]");
        //8a2bc67259abba740159af5599df000a
    }
    @Test
    public void test20(){
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = "-10";
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_condition设置-10]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_condition设置-10]");
        //8a2bc67259abba740159af5599df000a
    }
    @Test
    public void test21(){
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = "5.5";
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_condition设置5.5]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_condition设置5.5]");
        //8a2bc67259abba740159af5599df000a
    }
    @Test
    public void test22(){
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = "5";
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_queue设置]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_queue设置]");
        //8a2a6a4a59af56030159af57d50b0000
    }
    @Test
    public void test23(){
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = "-10";
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_queue设置-10]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_queue设置-10]");
        //8a2a6a4a59af56030159af57d50b0000
    }
    @Test
    public void test24(){
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = "5.5";
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_queue设置5.5]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_queue设置5.5]");
        //8a2a6a4a59af56030159af57d50b0000
    }
    @Test
    public void test25(){
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][remark为空]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][remark为空]");
        //8a2a6a4a59af56030159af57d50b0000
    }
    @Test
    public void test26(){
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = "ffffffffffffffffff";
        logger.info("[开始][remark填写]");
        CallcenterChannelNew( appId,  maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][remark填写]");
        //8a2bc67259af56370159af5a235b0001
    }
    public static void  CallcenterChannelEdit(String appId,String channelId ,String maxAgent,String maxSkill,String maxCondition,String maxQueue,String remark)  {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("max_agent",maxAgent);
        params.put("max_skill",maxSkill);
        params.put("max_condition",maxCondition);
        params.put("max_queue",maxQueue);
        params.put("remark",remark);
        logger.info("[查询通道]参数：" + params);
        try {
            String result = HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_CHANNEL+"/"+channelId),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test27(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = "10";
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_agent设置]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_agent设置]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test28(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = "-10";
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_agent设置-10]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_agent设置-10]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test29(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = "5.5";
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_agent设置5.5]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_agent设置5.5]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test30(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = "5";
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_skill设置]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_skill设置]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test31(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = "-5.5";
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_skill设置-10]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_skill设置-10]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test32(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = "5.5";
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_skill设置5.5]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_skill设置5.5]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test33(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = "5";
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_condition设置]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_condition设置]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test34(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = "-10";
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_condition设置-10]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_condition设置-10]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test35(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = "5.5";
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][max_condition设置5.5]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_condition设置5.5]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test36(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = "5";
        String remark = null;
        logger.info("[开始][max_queue设置]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_queue设置]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test37(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = "-10";
        String remark = null;
        logger.info("[开始][max_queue设置-10]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_queue设置-10]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test38(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = "5.5";
        String remark = null;
        logger.info("[开始][max_queue设置5.5]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][max_queue设置5.5]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test39(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = "-10";
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = null;
        logger.info("[开始][remark为空]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][remark为空]");
        //8a2bc67259af56370159af5a235b0001
    }
    @Test
    public void test40(){
        String channelId = "8a2a6a4a59abba410159af4ce1640006";
        String maxAgent = null;
        String maxSkill = null;
        String maxCondition = null;
        String maxQueue = null;
        String remark = "kkkkkkkkkkkkkkk";
        logger.info("[开始][remark填写]");
        CallcenterChannelEdit( appId,  channelId,maxAgent, maxSkill, maxCondition, maxQueue, remark);
        logger.info("[结束][remark填写]");
        //8a2bc67259af56370159af5a235b0001
    }

    public static void  CallcenterConditionNew(String appId,String channel,String where,String sort,String priority,String queueTimeout,String fetchTimeout,String remark){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("channel",channel);
        params.put("where",where);
        params.put("sort",sort);
        params.put("priority",priority);
        params.put("queue_timeout",queueTimeout);
        params.put("fetch_timeout",fetchTimeout);
        params.put("remark",remark);
        logger.info("[新建排队条件]参数：" + params);
        try {
            String result = HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_CONDITION),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }


    public static void   CallcenterConditionEdit(String appId,String conditionId,String channel,String where,String sort,String priority,String queueTimeout,String fetchTimeout,String remark) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("channel",channel);
        params.put("where",where);
        params.put("sort",sort);
        params.put("priority",priority);
        params.put("queue_timeout",queueTimeout);
        params.put("fetch_timeout",fetchTimeout);
        params.put("remark",remark);
        logger.info("[修改排队条件]参数：" + params);
        try {
            String result = HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_CONDITION+"/"+conditionId),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    @Test
    public void test41(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"大家\") > 60;";
        String sort = null;
        String priority = "1";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][channel存在的通道id]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][channel存在的通道id]");
        //8a2bc67259af56370159b0409fd30003
    }
    @Test
    public void test42(){
        String channel = null;
        String where = "get(\"大家\") > 60;";
        String sort = null;
        String priority = "1";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][channel不存在的通道id]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][channel不存在的通道id]");
        //8a2bc67259af56370159b0409fd30003
    }
    @Test
    public void test43(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = null;
        String sort = null;
        String priority = "1";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][where为空]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][where为空]");
        //8a2bc67259af56370159b0409fd30003
    }
    @Test
    public void test44(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"大家\") > 60;";
        String sort = null;
        String priority = "1";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][where填写]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][where填写]");
        //8a2bc67259af56370159b0409fd30003
    }
    @Test
    public void test45(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"大家\") > 60;";
        String sort = null;
        String priority = "1";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][sort为空null]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][sort为空null]");
        //8a2bc67259af56370159b0409fd30003
    }
    @Test
    public void test46(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"sort填写\") > 60;";
        String sort = "get(\"sort填写\");";
        String priority = "1";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][sort填写]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][sort填写]");
        //8a2bc67259af56370159b0409fd30003
    }
    @Test
    public void test47(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"sort填写\") > 60;";
        String sort = "get(\"sort填写\");";
        String priority = null;
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][sort填写]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][sort填写]");
        //8a2a6a4a59af56030159b04969990001
    }
    @Test
    public void test48(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"sort填写\") > 60;";
        String sort = "get(\"sort填写\");";
        String priority = "50";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][priority设置50]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][priority设置50]");
        //8a2a6a4a59af56030159b04ac32a0002
    }
    @Test
    public void test49(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"priority设置-1\") > 60;";
        String sort = "get(\"priority设置-1\");";
        String priority = "-1";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][priority设置-1]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][priority设置-1]");
        //8a2a6a4a59af56030159b04ac32a0002
    }
    @Test
    public void test50(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"priority设置100\") > 60;";
        String sort = "get(\"priority设置100\");";
        String priority = "100";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][priority设置100]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][priority设置100]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test51(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"priority设置5.5\") > 60;";
        String sort = "get(\"priority设置5.5\");";
        String priority = "5.5";
        String queueTimeout = "15";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][priority设置5.5]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][priority设置5.5]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test52(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"queue_timeout设置0\") > 60;";
        String sort = "get(\"queue_timeout设置0\");";
        String priority = null;
        String queueTimeout = null;
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][queue_timeout为空]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][queue_timeout为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test53(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"queue_timeout设置1\") > 60;";
        String sort = "get(\"queue_timeout设置1\");";
        String priority = null;
        String queueTimeout = "1";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][queue_timeout设置1]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][queue_timeout设置1]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test54(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"queue_timeout设置1001\") > 60;";
        String sort = "get(\"queue_timeout设置1001\");";
        String priority = null;
        String queueTimeout = "1001";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][queue_timeout设置1001]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][queue_timeout设置1001]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test55(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"queue_timeout设置-10\") > 60;";
        String sort = "get(\"queue_timeout设置-10\");";
        String priority = null;
        String queueTimeout = "-10";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][queue_timeout设置-10]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][queue_timeout设置-10]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test56(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"queue_timeout设置5.5\") > 60;";
        String sort = "get(\"queue_timeout设置5.5\");";
        String priority = null;
        String queueTimeout = "5.5";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][queue_timeout设置5.5]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][queue_timeout设置5.5]");
        //8a2a6a4a59b04bb80159b04cda380000
    }

    @Test
    public void test57(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"fetch_timeout为空\") > 60;";
        String sort = "get(\"fetch_timeout为空\");";
        String priority = null;
        String queueTimeout = "1";
        String fetchTimeout = null;
        String remark = null;
        logger.info("[开始][fetch_timeout为空]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][fetch_timeout为空]");
        //8a2a6a4a59b04bb80159b06096de001a
    }
    @Test
    public void test58(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"fetch_timeout设置0\") > 60;";
        String sort = "get(\"fetch_timeout设置0\");";
        String priority = null;
        String queueTimeout = "1";
        String fetchTimeout = "0";
        String remark = null;
        logger.info("[开始][fetch_timeout设置0]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][fetch_timeout设置0]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test59(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"fetch_timeout设置10\") > 60;";
        String sort = "get(\"fetch_timeout设置10\");";
        String priority = null;
        String queueTimeout = "5";
        String fetchTimeout = "10";
        String remark = null;
        logger.info("[开始][fetch_timeout设置10]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][fetch_timeout设置10]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test60(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"fetch_timeout设置-10\") > 60;";
        String sort = "get(\"fetch_timeout设置-10\");";
        String priority = null;
        String queueTimeout = "5";
        String fetchTimeout = "-10";
        String remark = null;
        logger.info("[开始][fetch_timeout设置-10]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][fetch_timeout设置-10]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test61(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"fetch_timeout设置5.5\") > 60;";
        String sort = "get(\"fetch_timeout设置5.5\");";
        String priority = null;
        String queueTimeout = "5";
        String fetchTimeout = "5.5";
        String remark = null;
        logger.info("[开始][fetch_timeout设置5.5]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][fetch_timeout设置5.5]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test62(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"queue_timeout设置5.5\") > 60;";
        String sort = "get(\"queue_timeout设置5.5\");";
        String priority = null;
        String queueTimeout = "5";
        String fetchTimeout = "5";
        String remark = null;
        logger.info("[开始][remark为空]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][remark为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test63(){
        String channel = "8a2a6a4a59abba410159af4ce1640006";
        String where = "get(\"remark填写\") > 60;";
        String sort = "get(\"remark填写\");";
        String priority = null;
        String queueTimeout = "5";
        String fetchTimeout = "5";
        String remark = "5555";
        logger.info("[开始][remark填写]");
        CallcenterConditionNew( appId, channel, where, sort, priority, queueTimeout, fetchTimeout, remark);
        logger.info("[结束][remark填写]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    public static void   CallcenterAgentLogin(String appId, String name, String channel, String num, String state, List<AgentSkill> skills, String extension)  {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("name",name);
        params.put("channel",channel);
        params.put("num",num);
        params.put("state",state);
        if(skills != null) {
            params.put("skills", skills.toArray());
        }else{
            params.put("skills", null);
        }
        params.put("extension",extension);
        logger.info("[坐席登录]参数：" + params);
        try {
            String result = HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_AGENT),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    @Test
    public void test64(){
        String name = null;
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][name为空]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][name为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test65(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][name唯一]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][name唯一]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test66(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][name重复]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][name重复]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test67(){
        String name = "zhangxb";
        String channel = null;
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][channel为空]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][channel为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test68(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e00456451c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][channel指定存在的通道id]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][channel指定存在的通道id]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test69(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][channel指定不存在的通道id]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][channel指定不存在的通道id]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test70(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = null;
        logger.info("[开始][extension为空]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][extension为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test71(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][extension指定存在的分机id]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][extension指定存在的分机id]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test72(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159a4546be26b760000";
        logger.info("[开始][extension指定不存在的分机id]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][extension指定不存在的分机id]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test73(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][num为空]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][num为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test74(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = "123456";
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][num填写]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][num填写]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test75(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][state为空]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][state为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test76(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = "busy";
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][state设置为busy]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][state设置为busy]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test77(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = "away";
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][state设置为away]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][state设置为away]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test78(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = "idle";
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  80,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][state设置为idle]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][state设置为idle]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test79(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = "fetching";
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][state设置为fetching]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][state设置为fetching]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test80(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = "talking";
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][state设置为talking]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][state设置为talking]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test81(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = null;
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][skills为空]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][skills为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test82(){
        String name = "zhangxb";
        String channel = "8a2a6a4a59b04bb80159b0ebbd8e001c";
        String num = null;
        String state = null;
        List<AgentSkill> skills = new ArrayList<AgentSkill>(){
            {
                add( new AgentSkill( "普通话" ,  60,  true) );
            }
        };
        String extension = "8a2a6a4a59abba410159abe26b760000";
        logger.info("[开始][skills设置技能]");
        CallcenterAgentLogin(  appId,  name,  channel,  num,  state,  skills,  extension);
        logger.info("[结束][skills设置技能]");
        //8a2a6a4a59b04bb80159b04cda380000
    }

    public static void   CallcenterAgentLogout(String appId, String agentName, Boolean force) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("force",force);
        logger.info("[坐席注销]参数：" + params);
        try {
            String result = HttpClientUtils.doDelete(sapiSdk.getUrl(SapiConstants.CALLCENTER_AGENT+"/"+agentName),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }


    @Test
    public void test83(){
        String agentName = "zhangxb";
        Boolean force = null;
        logger.info("[开始][force为空]");
        CallcenterAgentLogout(  appId,  agentName,  force );
        logger.info("[结束][force为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test84(){
        String agentName = "zhangxb";
        Boolean force = true;
        logger.info("[开始][force填写]");
        CallcenterAgentLogout(  appId,  agentName,  force );
        logger.info("[结束][force填写]");
        //8a2a6a4a59b04bb80159b04cda380000
    }

    public static void  CallcenterAgentExtension(String appId, String agentName,String id) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",id);
        logger.info("[设置坐席分机]参数：" + params);
        try {
            String result =  HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_AGENT_EXTENSION,agentName),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test85(){
        String agentName = "zhangxb";
        String id = null;
        logger.info("[开始][分机id为空]");
        CallcenterAgentExtension(  appId,  agentName,  id );
        logger.info("[结束][分机id为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test86(){
        String agentName = "zhangxb";
        String id = "sdfsdf";
        logger.info("[开始][分机id不存在]");
        CallcenterAgentExtension(  appId,  agentName,  id );
        logger.info("[结束][分机id不存在]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test87(){
        String agentName = "zhangxb";
        String id = "8a2a6a4a59b04bb80159b45b639c002c";
        logger.info("[开始][分机id存在]");
        CallcenterAgentExtension(  appId,  agentName,  id );
        logger.info("[结束][分机id存在]");
        //8a2a6a4a59b04bb80159b04cda380000
    }

    public static void CallcenterAgentState(String appId, String agentName,String state) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("state",state);
        logger.info("[设置坐席状态]参数：" + params);
        try {
            String result =   HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_AGENT_STATE,agentName),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test88(){
        String agentName = "zhangxb";
        String state = null;
        logger.info("[开始][state为空]");
        CallcenterAgentState(  appId,  agentName,  state );
        logger.info("[结束][state为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test89(){
        String agentName = "zhangxb";
        String state = "busy";
        logger.info("[开始][state设置为busy]");
        CallcenterAgentState(  appId,  agentName,  state );
        logger.info("[结束][state设置为busy]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test90(){
        String agentName = "zhangxb";
        String state = "away";
        logger.info("[开始][state设置为away]");
        CallcenterAgentState(  appId,  agentName,  state );
        logger.info("[结束][state设置为away]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test91(){
        String agentName = "zhangxb";
        String state = "idle";
        logger.info("[开始][state设置为idle]");
        CallcenterAgentState(  appId,  agentName,  state );
        logger.info("[结束][state设置为idle]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test92(){
        String agentName = "zhangxb";
        String state = "fetching";
        logger.info("[开始][state设置为fetching]");
        CallcenterAgentState(  appId,  agentName,  state );
        logger.info("[结束][state设置为fetching]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test93(){
        String agentName = "zhangxb";
        String state = "talking";
        logger.info("[开始][state设置为talking]");
        CallcenterAgentState(  appId,  agentName,  state );
        logger.info("[结束][state设置为talking]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test94(){
        String agentName = "zhangxb";
        String state = "xxxx";
        logger.info("[开始][state设置为未知状态]");
        CallcenterAgentState(  appId,  agentName,  state );
        logger.info("[结束][state设置为未知状态]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    public static void   CallcenterAgentSkills(String appId, String agentName,List<AgentSkillOperation> opts)  {
        Map<String,Object> params = new HashMap<String,Object>();
        if(opts == null ){
            params.put("opts",null);
        }else{
            params.put("opts",opts.toArray());
        }
        logger.info("[设置坐席技能]参数：" + params);
        try {
            String result =   HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_AGENT_SKILLS,agentName),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test95(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = null;
        logger.info("[开始][opts为空]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts为空]");
        //8a2a6a4a59b04bb80159b04cda380000
    }
    @Test
    public void test96(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation("",60,true,1));//空字符串和null都会
            }
        };
        logger.info("[开始][opts设置1，name为空]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置1，name为空]");
    }
    @Test
    public void test97(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation("技能1",60,true,1));
            }
        };
        logger.info("[开始][opts设置1，name填写]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置1，name填写]");
    }
    @Test
    public void test98(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation("技能1",null,true,1));
            }
        };
        logger.info("[开始][opts设置1，score为空]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置1，score为空]");
    }
    @Test
    public void test99(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation("技能1",1000,true,1));
            }
        };
        logger.info("[开始][opts设置1，score填写]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置1，score填写]");
    }
    @Test
    public void test100(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation("技能1",1000,true,1));
            }
        };
        logger.info("[开始][opts设置1，enabled填写]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置1，enabled填写]");
    }
    @Test
    public void test101(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation("技能1",1000,null,1));
            }
        };
        logger.info("[开始][opts设置1，enabled为空]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置1，enabled为空]");
    }
    @Test
    public void test102(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation(null,1000,null,2));
            }
        };
        logger.info("[开始][opts设置2，name为空]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置2，name为空]");
    }
    @Test
    public void test103(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation("技能1",100,null,2));
            }
        };
        logger.info("[开始][opts设置2，name填写]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置2，name填写]");
    }
    @Test
    public void test104(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation(null,100,null,0));
            }
        };
        logger.info("[开始][opts设置0，name为空]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置0，name为空]");
    }
    @Test
    public void test105(){
        String agentName = "zhangxb";
        List<AgentSkillOperation> opts = new ArrayList<AgentSkillOperation>(){
            {
                add( new AgentSkillOperation(null,100,null,10));
            }
        };
        logger.info("[开始][opts设置其他数字]");
        CallcenterAgentSkills(  appId,  agentName,  opts );
        logger.info("[结束][opts设置其他数字]");
    }
}
