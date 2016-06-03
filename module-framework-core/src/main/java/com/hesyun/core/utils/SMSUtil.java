package com.hesyun.core.utils;

import java.net.URLEncoder;

import com.hesyun.core.SystemConfig;

/**
 * 短信发送工具类
 * 
 * @author WangYun
 *
 */
public class SMSUtil {
	private static final String SMSUrl = SystemConfig.getProperty("sms.url", "http://sms.gknet.com.cn:8180/Service.asmx");
	
	/**
	 * 发送短信
	 */
	@SuppressWarnings("deprecation")
	public static String sendMessage(String[] phoneNumbers, String message) {
		String phones = "";
		for (String phoneNumber : phoneNumbers) {
			phones += phoneNumber + ";";
		}
		
		phones = phones.substring(0, phones.lastIndexOf(";"));
		
		String msgUrl = SMSUrl + "/SendMessageStr?Id=" + SystemConfig.getProperty("sms.id")
						+ "&Name="+SystemConfig.getProperty("sms.name")
						+ "&Psw="+SystemConfig.getProperty("sms.psw")
						+ "&Message="+URLEncoder.encode(message)
						+ "&Phone="+phones+"&Timestamp=0";
		
		String responseStr = HttpClientUtil.httpGet(msgUrl);
		
		return responseStr;
	}
	
	/**
	 * 发送短信验证码
	 */
	public static String sendValidateCodeMsg(String[] phoneNumbers, String validateCode) {
		String message = MessagesUtils.getMessageText("sms.validate.code.msg", validateCode);
		return sendMessage(phoneNumbers, message);
	}
	
	/**
	 * 获取短信余额
	 */
	public static String getBalance() {
		String msgUrl = SMSUrl + "/GetBalanceStr?Id=" + SystemConfig.getProperty("sms.id")
				+ "&Name="+SystemConfig.getProperty("sms.name")
				+ "&Psw="+SystemConfig.getProperty("sms.psw");
		String responseStr = HttpClientUtil.httpGet(msgUrl);
		return responseStr;
	}
	
	public static void main(String[] args) {
		String[] phoneNumbers = new String[]{"15972970515"};
		String ret = sendValidateCodeMsg(phoneNumbers, "8712");
		System.out.println(ret);
	}
}
