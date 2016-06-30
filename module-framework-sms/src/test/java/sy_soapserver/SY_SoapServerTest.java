/**
 * SY_SoapServerTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.3  Built on : May 30, 2016 (04:08:57 BST)
 */
package sy_soapserver;


/*
 *  SY_SoapServerTest Junit test case
 */
public class SY_SoapServerTest extends junit.framework.TestCase {

    /**
     * Auto generated test method
     */
    public void testsendmsg() throws java.lang.Exception {
        sy_soapserver.SY_SoapServerStub stub = new sy_soapserver.SY_SoapServerStub(); //the default implementation should point to the right endpoint

        sy_soapserver.SendmsgDocument sendmsg42 = (sy_soapserver.SendmsgDocument) getTestObject(sy_soapserver.SendmsgDocument.class);
        SendmsgDocument.Sendmsg sendmsg = (SendmsgDocument.Sendmsg)getTestObject(SendmsgDocument.Sendmsg.class);
        sendmsg.setUser("广州流水行云科技");
        sendmsg.setPasswd("lsxy123");
        sendmsg.setMsg("【云验证】验证码是123456");
        sendmsg.setPhone("13971068693");
        sendmsg42.setSendmsg(sendmsg);

        // TODO : Fill in the sendmsg42 here

        SendmsgResponseDocument result = stub.sendmsg(sendmsg42);
        System.out.println(result.getSendmsgResponse().getSendmsg());
    }


    public void testgetBalance() throws Exception {
        sy_soapserver.SY_SoapServerStub stub = new sy_soapserver.SY_SoapServerStub(); //the default implementation should point to the right endpoint
        GetbalanceDocument getBalanceDocument = (GetbalanceDocument) getTestObject(GetbalanceDocument.class);
        GetbalanceDocument.Getbalance getBalanceObj = (GetbalanceDocument.Getbalance)getTestObject(GetbalanceDocument.Getbalance.class);
        getBalanceObj.setUsername("广州流水行云科技");
        getBalanceObj.setPwd("lsxy123");
        getBalanceDocument.setGetbalance(getBalanceObj);
        GetbalanceResponseDocument responseDocument =  stub.getbalance(getBalanceDocument);
        System.out.println(responseDocument.getGetbalanceResponse().getGetbalance());
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
}
