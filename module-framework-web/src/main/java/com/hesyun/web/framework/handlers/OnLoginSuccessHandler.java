package com.hesyun.web.framework.handlers;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;


/**
 * 登录成功后的处理Handler
 * 1.生成hesyunToken cookie
 * @author tandy
 *
 */
public class OnLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	protected Log logger = LogFactory.getLog(getClass());  
	public OnLoginSuccessHandler(String targetUrl){
		this.setDefaultTargetUrl(targetUrl);
	}
	
	/**
	 * 登录成功后
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		logger.debug("user login success:"+authentication.getName());
//		try {
//			initContext(request,response,authentication);
//		} catch (MatchedMultiPersonException e) {
//			e.printStackTrace();
//		}
		super.onAuthenticationSuccess(request, response, authentication);

		//用户登录成功，此时无法获取到current信息，需要查询数据库获取当前用户信息
//		PersonManager personManager = (PersonManager)SpringContextUtil.getBean("personManager");
//		Person person = personManager.findPersonByEmail(authentication.getName());
//		//写入系统动态
//		XtdtManager xtdtManager = (XtdtManager) SpringContextUtil.getBean("xtdtManager");
//		Map<String,Object> contextMap = WebUtils.buildContextMap(request);
//		contextMap.put("p", person);
//		contextMap.put("tenant", person.getTenant());
//		xtdtManager.insertXtdt(contextMap,OperationEnum.OPERATE_PERSON_LOGIN,null);
	}

//	/**
//	 * 登录成功后初始化相关信息
//	 * @param request
//	 * @param response
//	 * @param authentication
//	 * @throws MatchedMultiPersonException
//	 */
//	public void initContext(HttpServletRequest request,HttpServletResponse response,Authentication authentication) throws MatchedMultiPersonException{
//		String username = authentication.getName();
//		logger.debug("login username :"+username);
//		Object currentUser = request.getSession().getAttribute("currentUser");
//			if(currentUser != null){
//				logger.debug("currentUser != null");
//				SecurityUser su = (SecurityUser) currentUser;
//				if(!su.getLoginName().equals(username)){
//					currentUser = null;
//				}
//			}
//			
//			if(currentUser == null){
//				logger.debug("currentUser == null");
//				if(StringUtil.isNotEmpty(username)){
//					PersonManager personManager = (PersonManager) SpringContextUtil.getBean("personManager");
//					Person person = personManager.findPersonByLoginName(username);
//					logger.debug("found person:"+person);
//					if(person != null) {
//						String userName = person.getUserName();
//						if (!"admin".equals(userName)) {
//							Tenant tenant = person.getTenant();
//							String tenantUn = "";
//							String tenantName = "";
//							if(tenant != null){
//								tenantUn = tenant.getTenantUn();
//								tenantName = tenant.getTenantName();
//							}
//							
//							Set<TenantRole> tenantRoles = person.getRoles();
//							Map<String, Boolean> authMap = new HashMap<String, Boolean>();
//							
//							for (TenantRole tenantRole : tenantRoles) {
//								Set<Permission> pers = tenantRole.getPermissions();
//								for (Permission per : pers) {
//									authMap.put(per.getModulePath(), true);
//								}
//							}
//							
//							SecurityUser user = new SecurityUser(person.getId(), person.getUserName(),person.getName(), tenantUn,tenantName, person.getRoleCodes());
//							user.setRoleNames(person.getRoleNames());
//							user.setNumber(person.getNumber());
//							user.setAuthMap(authMap);
//							
//							request.getSession().setAttribute("currentUser", user);
//							request.getSession().setAttribute("currentPerson", person);
//						} else {
//							SecurityUser user = new SecurityUser(person.getId(), person.getUserName(),person.getName(), "", "", "ROLE_ADMIN");
//							user.setRoleNames("和声管理员");
//							user.setNumber("0");
//							request.getSession().setAttribute("currentUser", user);
//							request.getSession().setAttribute("currentPerson", person);
//						}
//						this.createHesyunToken(response,authentication);
//					}
//				} 
//			}
//	}
//	
//	/**
//	 * 创建和声云Token cookie
//	 * @param response
//	 * @param authentication
//	 */
//	private void createHesyunToken(HttpServletResponse response,
//			Authentication authentication) {
//		String username = authentication.getName();
//		long time = new Date().getTime();
//		
//		try {
//			String encodeToken = EncryptDecryptData.encrypt(SystemConfig.getProperty("DES_ENCRYPT_KEY","376B4A409E5789CE"), time+"_"+username);
//			logger.debug("login token:"+encodeToken);
//			Cookie cookie = new Cookie("hesyuntoken", encodeToken);
//			cookie.setDomain(SystemConfig.getProperty("system.sso.domain",".hesyun.com"));
//			cookie.setHttpOnly(true);
//			cookie.setPath("/");
//			response.addCookie(cookie);
//		} catch (Exception ex){
//			ex.printStackTrace();
//		}
//	}

}
