/**
 * SY_SoapServerCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.3  Built on : May 30, 2016 (04:08:57 BST)
 */
package sy_soapserver;


/**
 *  SY_SoapServerCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class SY_SoapServerCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public SY_SoapServerCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public SY_SoapServerCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for get method
     * override this method for handling normal response from get operation
     */
    public void receiveResultget(GetResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from get operation
     */
    public void receiveErrorget(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for uppasswd method
     * override this method for handling normal response from uppasswd operation
     */
    public void receiveResultuppasswd(
        UppasswdResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from uppasswd operation
     */
    public void receiveErroruppasswd(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for __set method
     * override this method for handling normal response from __set operation
     */
    public void receiveResult__set(SetResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from __set operation
     */
    public void receiveError__set(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getreport method
     * override this method for handling normal response from getreport operation
     */
    public void receiveResultgetreport(
        GetreportResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getreport operation
     */
    public void receiveErrorgetreport(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getreply method
     * override this method for handling normal response from getreply operation
     */
    public void receiveResultgetreply(
        GetreplyResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getreply operation
     */
    public void receiveErrorgetreply(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for __isset method
     * override this method for handling normal response from __isset operation
     */
    public void receiveResult__isset(IssetResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from __isset operation
     */
    public void receiveError__isset(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for __get method
     * override this method for handling normal response from __get operation
     */
    public void receiveResult__get(GetResponseDocument2 result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from __get operation
     */
    public void receiveError__get(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for __destruct method
     * override this method for handling normal response from __destruct operation
     */
    public void receiveResult__destruct(
        DestructResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from __destruct operation
     */
    public void receiveError__destruct(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getbalance method
     * override this method for handling normal response from getbalance operation
     */
    public void receiveResultgetbalance(
        GetbalanceResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getbalance operation
     */
    public void receiveErrorgetbalance(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for __call method
     * override this method for handling normal response from __call operation
     */
    public void receiveResult__call(CallResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from __call operation
     */
    public void receiveError__call(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sendmsg method
     * override this method for handling normal response from sendmsg operation
     */
    public void receiveResultsendmsg(
        SendmsgResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sendmsg operation
     */
    public void receiveErrorsendmsg(Exception e) {
    }
}
