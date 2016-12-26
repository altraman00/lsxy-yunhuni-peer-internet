package com.lsxy.framework.sms.clients;

import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sy_soapserver.GetbalanceDocument;
import sy_soapserver.GetbalanceResponseDocument;
import sy_soapserver.SendmsgDocument;
import sy_soapserver.SendmsgResponseDocument;

import static com.lsxy.framework.mq.ons.OnsMQService.logger;

/**
 * Created by Tandy on 2016/7/7.
 * 圣亚短信通客户端
 */
public class SMSClientSY implements SMSClient {
    private static final Logger logger = LoggerFactory.getLogger(SMSClientSY.class);
    private String userName = "广州流水行云科技";
    private String password = "lsxy123";

    sy_soapserver.SY_SoapServerStub stub ;
    public SMSClientSY(){
        try {
            stub = new sy_soapserver.SY_SoapServerStub(); //the default implementation should point to the right endpoint
        } catch (AxisFault axisFault) {
            logger.error("异常",axisFault);
        } catch (Exception e) {
            logger.error("异常",e);
        }
    }

    @Override
    public String sendsms(String to, String msg) {
        try {
            SendmsgDocument.Sendmsg sendmsg =  (SendmsgDocument.Sendmsg)getTestObject(SendmsgDocument.Sendmsg.class);;
            SendmsgDocument sendmsg42 = (SendmsgDocument) getTestObject(SendmsgDocument.class);;
            sendmsg.setUser(userName);
            sendmsg.setPasswd(password);
            sendmsg.setMsg(msg);
            sendmsg.setPhone(to);
            sendmsg42.setSendmsg(sendmsg);
            //发短信方法
            SendmsgResponseDocument result = stub.sendmsg(sendmsg42);
            return result.getSendmsgResponse().getSendmsg();
        } catch (Exception e) {
            logger.error("发送短信异常",e);
        }
        return null;
    }

    @Override
    public int balance() {
        int result = 0;
        GetbalanceDocument getBalanceDocument = null;
        try {
            getBalanceDocument = (GetbalanceDocument) getTestObject(GetbalanceDocument.class);
            GetbalanceDocument.Getbalance getBalanceObj = (GetbalanceDocument.Getbalance)getTestObject(GetbalanceDocument.Getbalance.class);
            getBalanceObj.setUsername(userName);
            getBalanceObj.setPwd(password);
            getBalanceDocument.setGetbalance(getBalanceObj);
            GetbalanceResponseDocument responseDocument =  stub.getbalance(getBalanceDocument);
            result = Integer.parseInt(responseDocument.getGetbalanceResponse().getGetbalance());
        } catch (Exception e) {
            logger.error("异常",e);
        }
        return result;
    }

    @Override
    public String getClientName() {
        return "圣亚短信通";
    }


    //Create the desired XmlObject and provide it as the test object
    public org.apache.xmlbeans.XmlObject getTestObject(java.lang.Class type)
            throws java.lang.Exception {
        java.lang.reflect.Method creatorMethod = null;

        if (org.apache.xmlbeans.XmlObject.class.isAssignableFrom(type)) {
            Class[] declaredClasses = type.getDeclaredClasses();

            for (int i = 0; i < declaredClasses.length; i++) {
                Class declaredClass = declaredClasses[i];

                if (declaredClass.getName().endsWith("$Factory")) {
                    creatorMethod = declaredClass.getMethod("newInstance", null);

                    break;
                }
            }
        }
        if (creatorMethod != null) {
            return (org.apache.xmlbeans.XmlObject) creatorMethod.invoke(null,
                    null);
        } else {
            throw new java.lang.Exception("Creator not found!");
        }
    }

    public static void main(String[] args) {
        SMSClientSY client = new SMSClientSY();
        String result = client.sendsms("18826474526","【壹耘】您的验证码是0000");
        System.out.println("发送结果 :" + result);
    }

}
