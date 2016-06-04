package com.hesyun.framework.events.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.hesyun.framework.events.AbstractCommonEvent;
import com.lsxy.framework.config.SystemConfig;

/**
 * echo 事件
 * 包含事件发出的主机以及系统标识
 * @author tandy
 *
 */
public class MQEchoEvent extends AbstractCommonEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String systemid;
	private String host;
	
	public String getSystemid() {
		return systemid;
	}

	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public MQEchoEvent(){
		this.systemid = SystemConfig.getProperty("system.id","");
		InetAddress netAddress = getInetAddress();  
		this.host = getHostIp(netAddress);
	}
	
	public static InetAddress getInetAddress(){  
		  
        try{  
            return InetAddress.getLocalHost();  
        }catch(UnknownHostException e){  
            System.out.println("unknown host!");  
        }  
        return null;  
  
    }  
  
    public static String getHostIp(InetAddress netAddress){  
        if(null == netAddress){  
            return null;  
        }  
        String ip = netAddress.getHostAddress(); //get the ip address  
        return ip;  
    }  
    
    public static void main(String[] args) {
    	MQEchoEvent echo = new MQEchoEvent();
    	System.out.println(echo.toString());
	}
}
