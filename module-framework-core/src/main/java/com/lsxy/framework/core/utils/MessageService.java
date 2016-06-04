package com.lsxy.framework.core.utils;


/**
 * 短信调用接口
 * @author lijing
 *
 */
public class MessageService {

//	private static MessageService messageService;
//	private long smId = Long.parseLong(SystemConfig.getProperty("sms.smid"));
//	private String host = SystemConfig.getProperty("sms.host");
//	private String dbName =SystemConfig.getProperty("sms.dbname");
//	private String apiId =SystemConfig.getProperty("sms.apiid");
//	private String name =SystemConfig.getProperty("sms.username");
//	private String pwd =SystemConfig.getProperty("sms.password");
//	private APIClient handler = new APIClient();
//	
//	public MessageService() throws SMSInvokeException{
//		init();
//	}
//	
//	public static MessageService getInstance() throws SMSInvokeException{
//		if(messageService==null){
//			messageService =new MessageService();
//		}
//		return messageService;
//	}
//	
//	/**
//	 * 初始化信息
//	 * @throws SMSInvokeException 
//	 */
//	public void init() throws SMSInvokeException{
//		int connectRe = handler.init(host, name, pwd, apiId,dbName);
//        String msg = null;
//        SMSInvokeException ex = null;
//		if(connectRe == APIClient.IMAPI_CONN_ERR)
//        	ex = new SMSInvokeException("短信服务连接失败,请稍候再试或联系管理员");
//        else if(connectRe == APIClient.IMAPI_API_ERR)
//        	ex = new SMSInvokeException("短信服务初始化失败，请稍候再试或联系管理员");
//        if(ex != null)
//        	throw ex;
//	}
//	
//	/**
//	 * 发送短信
//	 * @param mobiles
//	 * @param content
//	 */
//	public void sendSM(String mobile,long randomNum){
//		String[] mobiles = new String[]{mobile};
//		String content="欢迎您使用统一用户管理平台，您的账户激活码是"+randomNum+"，请您尽快完成账户激活！";
//		int result = 0;
//		result = handler.sendSM(mobiles, content, smId , Long.parseLong("" + smId));
//		if(result == APIClient.IMAPI_SUCC){            
//			System.out.println("发送成功！\n");
//		}
//		else if(result == APIClient.IMAPI_INIT_ERR)
//			System.out.println("未初始化！");
//		else if(result == APIClient.IMAPI_CONN_ERR)
//			System.out.println("数据库连接失败！");
//		else if(result == APIClient.IMAPI_DATA_ERR)
//			System.out.println("参数错误！");
//		else if(result == APIClient.IMAPI_DATA_TOOLONG)
//			System.out.println("消息内容太长！");
//		else if(result == APIClient.IMAPI_INS_ERR)
//			System.out.println("数据库插入错误！");
//		else
//			System.out.println("出现其他错误！");
//	}
//	
//	public void usage(){
//		System.out.println("Usage : java ApiTestDemo [-h host] [-n name] [-p password] [-i apiCode]");
//		System.out.println("\t-h host        信息机地址");
//		System.out.println("\t-n name        API登陆名");
//		System.out.println("\t-p password    API登陆密码");
//		System.out.println("\t-i apiCode     API编码");
//	}
//	
//	
//	public static void main(String args[]) throws SMSInvokeException{
//		MessageService ms=MessageService.getInstance();	
//		long randomNum=RandomNumberUtil.randomLong(5);
//		String mobile="13971068693";
//		ms.sendSM(mobile,randomNum);
//	}
}
