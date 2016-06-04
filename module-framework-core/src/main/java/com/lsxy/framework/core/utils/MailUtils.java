package com.lsxy.framework.core.utils;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.exceptions.MailConfigNotEnabledException;
import com.lsxy.framework.core.exceptions.MailContentNullException;

/**
 * Mail实体类(包含发件功能)
 */
public class MailUtils {

	protected static Log logger = LogFactory.getLog(MailUtils.class);
	public MailUtils() {
	}
	

	/**
	 * 把主题转换为中文
	 * 
	 * @param strText
	 * @return
	 */
	public static String transferChinese(String strText) {

		try {
			strText = MimeUtility.encodeText(strText, "UTF-8", "B");	
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strText;
	}
	
	
	/**
	 * 发送邮件
	 * @param type  0-根据验证码  1-根据安全链接
	 * @return 成功返回true，失败返回false
	 */
	public  static boolean sendMail(String title, String email,String emailContent) throws MailConfigNotEnabledException  {
		if (SystemConfig.getProperty("mail.enabled", "true").equals("false")) {
			throw new MailConfigNotEnabledException();
		}
		logger.debug("给"+email+"发邮件");
		logger.debug("邮件内容:"+emailContent);
		// 构造mail session
		Properties props = System.getProperties();
		props.put("mail.smtp.host", SystemConfig.getProperty("mail.smtp.host"));
		props.put("mail.smtp.port", SystemConfig.getProperty("mail.smtp.port","25"));
		props.put("mail.smtp.auth", "true");
//		if(SystemConfig.getProperty("http.proxy.enable","false").equals("true")){
//			String proxyhost = SystemConfig.getProperty("http.proxy.hostname");
//			String port = SystemConfig.getProperty("http.proxy.port");
//			logger.debug("使用代理："+proxyhost + ":" + port);
//			props.put("http.proxySet","true");//设置代理主机参数  
//			props.put("http.proxyHost",proxyhost);  
//			props.put("http.proxyPort",port);
//		}
		
		Session session = Session.getInstance(props,
				new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(SystemConfig.getProperty("mail.sender"), SystemConfig.getProperty("mail.password"));
					}
				});
		try {
			// 构造MimeMessage并设定基本的值，创建消息对象
			MimeMessage msg = new MimeMessage(session);
			// 设置消息内容
			msg.setFrom(new InternetAddress(SystemConfig.getProperty("mail.sender")));
			// 把邮件地址映射到Internet地址上
			InternetAddress[] address = { new InternetAddress(email) };
			/**
			 * setRecipient（Message.RecipientType type, Address
			 * address），用于设置邮件的接收者。<br>
			 * 有两个参数，第一个参数是接收者的类型，第二个参数是接收者。<br>
			 * 接收者类型可以是Message.RecipientType .TO，Message
			 * .RecipientType.CC和Message.RecipientType.BCC，TO表示主要接收人，CC表示抄送人
			 * ，BCC表示秘密抄送人。接收者与发送者一样，通常使用InternetAddress的对象。
			 */
			msg.setRecipients(Message.RecipientType.TO, address);
			// 设置邮件的标题
			String subject = transferChinese(title);
			msg.setSubject(subject);
			// 构造Multipart
			Multipart mp = new MimeMultipart();

			// 向Multipart添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			// 设置邮件内容(纯文本格式)
			// mbpContent.setText(content);
			mbpContent.setContent(emailContent, "text/html;charset=utf-8");
			// 向MimeMessage添加（Multipart代表正文）
			mp.addBodyPart(mbpContent);
			// 向Multipart添加MimeMessage
			msg.setContent(mp);
			// 设置邮件发送的时间。
			msg.setSentDate(new Date());
			// 发送邮件
			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 根据模板发送邮件（模板文件请放置在templateFile）
	 * @param temName test.vm 模板第一行为标题 后面为内容
	 * @param params 模板中对应参数信息
	 * @param email  test@qq.com
	 * @return
	 * @throws MailContentNullException 
	 */
	public static boolean sendMailByTemplate(String temName,Map<String,String> params,String email) 
			throws MailConfigNotEnabledException, MailContentNullException{
		Properties properties = new Properties();
		properties.setProperty("resource.loader", "class");
		properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine velocityEngine = new VelocityEngine(properties);
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();
		for(String key : params.keySet()){
			context.put(key, params.get(key));
		}	
		velocityEngine.mergeTemplate("emailTemplate/" + temName, "utf-8", context, writer);	
		String tmpStr = writer.toString();
		String emailContent = tmpStr; //邮件内容
		if(StringUtil.isEmpty(tmpStr)){
			throw new MailContentNullException();
		}
		String emailTitile = tmpStr.substring(0,tmpStr.indexOf("\n"));
		if(emailTitile.startsWith("title:")){
			emailTitile = emailTitile.substring(6);
		}else{
			emailTitile = SystemConfig.getProperty("mail.title");//获取邮件title
		}
		emailContent = tmpStr.substring(tmpStr.indexOf("\n") + 1,tmpStr.length());			
		if (SystemConfig.getProperty("mail.enabled", "true").equals("false")) {
			throw new MailConfigNotEnabledException();
		}
		logger.debug("给"+email+"发邮件");
		logger.debug("邮件内容:"+emailContent);
		// 构造mail session
		Properties props = System.getProperties();
		props.put("mail.smtp.host", SystemConfig.getProperty("mail.smtp.host"));
		props.put("mail.smtp.port", SystemConfig.getProperty("mail.smtp.port","25"));
		props.put("mail.smtp.auth", "true");	
		Session session = Session.getInstance(props,
				new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(SystemConfig.getProperty("mail.sender"), SystemConfig.getProperty("mail.password"));
					}
				});
		try {
			// 构造MimeMessage并设定基本的值，创建消息对象
			MimeMessage msg = new MimeMessage(session);
			// 设置消息内容
			msg.setFrom(new InternetAddress(SystemConfig.getProperty("mail.sender"), SystemConfig.getProperty("mail.senderName")));
			// 把邮件地址映射到Internet地址上
			InternetAddress[] address = { new InternetAddress(email, SystemConfig.getProperty("mail.senderName")) };
			/**
			 * setRecipient（Message.RecipientType type, Address
			 * address），用于设置邮件的接收者。<br>
			 * 有两个参数，第一个参数是接收者的类型，第二个参数是接收者。<br>
			 * 接收者类型可以是Message.RecipientType .TO，Message
			 * .RecipientType.CC和Message.RecipientType.BCC，TO表示主要接收人，CC表示抄送人
			 * ，BCC表示秘密抄送人。接收者与发送者一样，通常使用InternetAddress的对象。
			 */
			msg.setRecipients(Message.RecipientType.TO, address);
			// 设置邮件的标题
			String subject = transferChinese(emailTitile);
			msg.setSubject(subject);
			
			// 构造Multipart
			Multipart mp = new MimeMultipart();

			// 向Multipart添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			// 设置邮件内容(纯文本格式)
			// mbpContent.setText(content);
			mbpContent.setContent(emailContent, "text/html;charset=utf-8");
			// 向MimeMessage添加（Multipart代表正文）
			mp.addBodyPart(mbpContent);
			// 向Multipart添加MimeMessage
			msg.setContent(mp);
			// 设置邮件发送的时间。
			msg.setSentDate(new Date());
			// 发送邮件
			Transport.send(msg);
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
}
