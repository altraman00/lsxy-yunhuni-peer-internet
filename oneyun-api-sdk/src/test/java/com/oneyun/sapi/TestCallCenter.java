package com.oneyun.sapi;

import com.oneyun.sapi.config.SapiConstants;
import com.oneyun.sapi.utils.HttpClientUtils;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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



    public static void   CallcenterConditionEdit(String appId,String conditionId,String channel,String where,String sort,Integer priority,Integer queueTimeout,Integer fetchTimeout,String remark) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
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
            String result = HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALLCENTER_CONDITION+"/"+conditionId),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]" + result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }


}
