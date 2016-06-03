package com.hesyun.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hesyun.core.exceptions.ConfigFileNotExistException;
import com.hesyun.core.utils.PropertiesUtil;
import com.hesyun.core.utils.StringUtil;

/**
 * 读取系统配置的工具方法
 * @author tanchang
 *
 */
public class SystemConfig {
	private static Log logger=LogFactory.getLog(SystemConfig.class);
	private static PropertiesUtil pp;
	static{
		URL url = SystemConfig.class.getClassLoader().getResource(Constants.DEFAULT_CONFIG_FILE);
		if(url != null){
			String file = url.getFile();
			file = file.replaceAll("%20", " ");
			pp = new PropertiesUtil(file,Constants.DEFAULT_CONFIG_FILE_ENCODING);
			File filex =new File(file);
			SystemConfig.setProperty("lastmodified", String.valueOf(filex.lastModified()));
		}
	}
	
	/**
	 *描述：重载全局配置
	 *时间：2010-1-11
	 *作者：谭畅
	 *参数：无
	 *返回值:无
	 *抛出异常：无
	 */
	public static void reload(){
		if(isConfigModified()){
			logger.debug("reload config properties file");
			URL url = SystemConfig.class.getClassLoader().getResource("config.properties");
			String file = url.getFile();
			file = file.replaceAll("%20", " ");
			pp = new PropertiesUtil(file,Constants.DEFAULT_CONFIG_FILE_ENCODING);
			File filex =new File(file);
			SystemConfig.setProperty("lastmodified", String.valueOf(filex.lastModified()));
		}
	}
	
	/**
	 * 判断属性文件是否被修改
	 * @return
	 */
	public static boolean isConfigModified(){
		URL url = SystemConfig.class.getClassLoader().getResource("config.properties");
		String file = url.getFile();
		file = file.replaceAll("%20", " ");
		File filex = new File(url.getFile());
		long lastModified = filex.lastModified();
		long curLastModified = Long.parseLong(SystemConfig.getProperty("lastmodified","0"));
//		logger.debug("lastModified:"+lastModified);
//		logger.debug("curLastModified:"+curLastModified);
		return lastModified != curLastModified;
	}
	

	
	/**
	 *描述：取得配置文件中的所有配置项，并以MAP形式返回
	 *时间：2010-1-11
	 *作者：谭畅
	 *参数：无
	 *返回值:无
	 *抛出异常：
	 */
	public static Map<String,String> getAllPropertiesInMap(){
		Map<String,String> resultMap = new HashMap<String,String>();
		for (Object key : pp) {
			String sKey = key.toString();
			resultMap.put(sKey, pp.getProperties(sKey));
		}
		return resultMap;
	}
	
	/**
	 * 
	 *描述：设置并保存Map中指定的配置信息
	 *时间：2010-1-12
	 *作者：谭畅
	 *参数：
	 *	@param propMap 参数键值对集合
	 *返回值:
	 *	无
	 *抛出异常：
	 * @throws Exception 
	 */
	public static void setAndSaveProperties(Map<String,String> propMap) throws Exception{
		URL url = SystemConfig.class.getClassLoader().getResource(Constants.DEFAULT_CONFIG_FILE);
		if(url==null)
			throw new ConfigFileNotExistException();
		String file = url.getFile();
		file = file.replaceAll("%20", " ");
		PropertiesUtil pu = new PropertiesUtil(new File(file), Constants.DEFAULT_CONFIG_FILE_ENCODING);
		for (String key : propMap.keySet()) {
			pu.setProperties(key, propMap.get(key));
		}
		pu.save();
		reload();
	}

	public static void setAndSaveProperties(String propertName, String propertValue) throws Exception{
		URL url = SystemConfig.class.getClassLoader().getResource("config.properties");
		if(url==null)
			throw new ConfigFileNotExistException();
		PropertiesUtil pu = new PropertiesUtil(new File(url.getFile()),Constants.DEFAULT_CONFIG_FILE_ENCODING);
		pu.setProperties(propertName, propertValue);
		pu.save();
		reload();
	}
	
	
	/**
	 * 
	 *描述：取得指定属性的值
	 *时间：2010-1-12
	 *作者：谭畅
	 *参数：
	 *	@param name 参数名称
	 *返回值:
	 *	@return 返回对应属性的值
	 *抛出异常：
	 */
	public static String getProperty(String name){
		return getProperty(name, null);
	}
	
	/**
	 * 
	 *描述：取得指定属性的值,如果没有配置该值，则给出一个默认的配置值
	 *时间：2010-1-12
	 *作者：谭畅
	 *参数：
	 *	@param name 参数名称
	 *返回值:
	 *	@return 返回对应属性的值
	 *抛出异常：
	 */
	public static String getProperty(String name,String defaultValue){
		String value = null;
		if(pp != null){
			value = pp.getProperties(name);
		}
		if(StringUtil.isNotEmpty(value)){
			value = value.trim();
		}else{
			value = defaultValue;
		}
		return value;
	}
	/**
	 * 设置配置
	 * @param name
	 * @param value
	 */
	public static void setProperty(String name,String value){
		pp.setProperties(name, value);
	}
	
	/**
	 * 保存配置文件
	 * @throws IOException
	 */
	public static void save() throws IOException{
		pp.save();
	}
	

	/**
	 * 是否debug模式
	 * @return
	 */
	public static boolean isDebug(){
		return getProperty("system.debug","false").equals("true");
	}
	/**
	 * 是否开发模式
	 * @return
	 */
	public static boolean isDevelopModel(){
		return getProperty("system.developModel","false").equals("true");
	}
	public static void main(String[] args) throws Exception {
		//Map map = new HashMap<String, String>();
		//map.put("aa", "bb");
		//SystemConfig.setAndSaveProperties(map);
//		String xx = SystemConfig.getProperty("JTEAP_SYSTEM_POPUP_WINDOW");
		
//		System.out.println(xx);
		
		
	}
	
	/**
	 * 获取版本时间戳
	 * 版本时间戳主要用于脚本文件或者css样式等静态文件添加后缀，如果在不改变的情况下，
	 * 时间戳也不发生变化，此时浏览器会加载浏览器缓存的静态文件
	 * 如果js文件发生变化，则需要强制浏览器从服务器端获取最新版本的js文件
	 * @return
	 */
	public static String getVersionStamp(){
		if(isDebug() || isDevelopModel()){
			return new Date().getTime()+"";
		}else{
			return SystemConfig.getProperty("system.versionStamp",new Date().getTime()+"");
		}
	}
	
}
