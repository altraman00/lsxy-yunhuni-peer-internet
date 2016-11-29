package com.lsxy.framework.mail;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.util.logging.Logger.global;

/**
 * Mail实体类(包含发件功能)
 */
@Component
public class MailService {
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	/**
	 * 把主题转换为中文
	 * 
	 * @param strText
	 * @return
	 */
	private String transferChinese(String strText) {

		try {
			strText = MimeUtility.encodeText(strText, "UTF-8", "B");	
		} catch (Exception e) {
			logger.error("设置编码异常",e);
		}

		return strText;
	}
	
	
	/**
	 * 发送邮件
	 * @return 成功返回true，失败返回false
	 */
	public  boolean send(String title, String mailto,String content) throws MailConfigNotEnabledException  {
		if (SystemConfig.getProperty("global.mail.enabled", "true").equals("false")) {
			throw new MailConfigNotEnabledException();
		}
		logger.debug("给"+mailto+"发邮件");
		// 构造mail session
		Properties props = System.getProperties();
		props.put("mail.smtp.host", SystemConfig.getProperty("global.mail.smtp.host"));
//		props.put("mail.smtp.port", SystemConfig.getProperty("global.mail.smtp.port","25"));
        props.put("mail.transport.protocol", "smtp");
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
						return new PasswordAuthentication(SystemConfig.getProperty("global.mail.sender.email"), SystemConfig.getProperty("global.mail.sender.password"));
					}
				});
		try {
			// 构造MimeMessage并设定基本的值，创建消息对象
			MimeMessage msg = new MimeMessage(session);
			//设置自定义发件人昵称
			String nickname = "壹耘";
			try {
				nickname = javax.mail.internet.MimeUtility.encodeText(SystemConfig.getProperty("global.mail.sender.nickname","壹耘"));
			} catch (UnsupportedEncodingException e) {
				logger.error("编码异常",e);
			}
			// 设置消息内容
			msg.setFrom(new InternetAddress(nickname + " <"+ SystemConfig.getProperty("global.mail.sender.email") + ">"));
			// 把邮件地址映射到Internet地址上
			InternetAddress[] address = { new InternetAddress(mailto) };
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
			mbpContent.setContent(content, "text/html;charset=utf-8");
			// 向MimeMessage添加（Multipart代表正文）
			mp.addBodyPart(mbpContent);
			// 向Multipart添加MimeMessage
			msg.setContent(mp);
			// 设置邮件发送的时间。
			msg.setSentDate(new Date());
			// 发送邮件
			Transport.send(msg);
		} catch (MessagingException e) {
			logger.error("设置消息体异常",e);
			return false;
		}
		return true;
	}
	
	/**
	 * 根据模板发送邮件（模板文件请放置在templateFile）
	 * @param template test.vm
	 * @param params 模板中对应参数信息
	 * @param mailto test@qq.com
	 * @return
	 * @throws MailContentNullException 
	 */
	public boolean send(String title,String mailto,String template, Map<String,String> params)
			throws MailConfigNotEnabledException, MailContentNullException{
		//根据模板构建邮件内容
		String content = buildMailContentInTemplate(template, params);
		return send(title,mailto,content);
	}

	/**
	 * 通过指定的模板构建邮件内容
	 * @param template
	 * @param params
     * @return
     */
	private String buildMailContentInTemplate(String template, Map<String, String> params) {
		Properties properties = new Properties();
		properties.setProperty("resource.loader", "class");
		properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngine velocityEngine = new VelocityEngine(properties);
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();
		for(String key : params.keySet()){
			context.put(key, params.get(key));
		}
		velocityEngine.mergeTemplate("mail_templates/" + template, "utf-8", context, writer);
		return writer.toString();
	}

    public static void main(String[] args) throws MailConfigNotEnabledException, MailContentNullException {
//        String title = "哈哈哈哈哈哈哈";
//        String url = "http://www.baidu.com";
//        Map<String, String> params = new HashMap<String, String>() {
//            {
//                put("link", url);
//            }
//        };
//        String mailto = "51562066@qq.com";
////        MailService.send(title,mailto,"这是邮件内容，测试使用的");
//        String template = "01-portal-notify-account-activate.vm";
//        MailService.send(title,mailto,template,params);
		System.out.println("\r\n".length());
    }
}
