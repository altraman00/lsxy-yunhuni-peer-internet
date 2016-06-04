package com.lsxy.app.portal.aop;

import com.lsxy.framework.core.utils.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

/**
 * 
 * @author tandy
 *
 */
@Aspect
@SuppressWarnings({"rawtypes" , "unchecked"})
public class LoggingAspect {
	
	/**
	 * 
	 */
//	@Autowired
//	private XtdtManager xtdtManager;
	
	/**
	 * 
	 * @param joinPoint
	 *            joinPoint
	 * @return 返回一个对象
	 * @throws Throwable
	 *  Throw
	 */
	@Around("within(com.lsxy.framework.core.web.AbstractController+) && "
			+ "@annotation(com.lsxy.app.portal.aop.LogTracker)")
	public final Object aroundSayHello(final ProceedingJoinPoint joinPoint)
			throws Throwable {
		Object obj = null;
		MethodSignature methodSignature = (MethodSignature) joinPoint
				.getSignature();
		Method targetMethod = methodSignature.getMethod();
		LogTracker lt = targetMethod.getAnnotation(LogTracker.class);
		HttpServletRequest request = this.getHttpRequest(joinPoint);

//		Xtdt xtdt = null;
//		try {
//			obj = joinPoint.proceed();
//			Map contextMap = WebUtils.buildContextMap(request);
//			xtdt = xtdtManager.insertXtdt(contextMap, lt.opType(), lt.value());
//			xtdt.setDetail(buildXtdtDetail(request, joinPoint, obj));
//		} catch (Exception ex) {
//			xtdt.setDetail(buildXtdtDetail(request, joinPoint, ex));
//		}
//		XtdtManager xtdtManager = (XtdtManager) SpringContextUtil
//				.getBean("xtdtManager");
//		xtdtManager.save(xtdt);
		return obj;
	}
	
	/**
	 * 构建系统动态的详细信息.
	 * 格式：
	 * target:com.hesyun.web.portal.aaaController.methodName
	 * url:http://xxxx/tenant/admin/restname
	 * param1:12341234
	 * param2:23lskjdlf
	 * return:234234234
	 * exception:234234lkjljljl
	 * @param joinPoint
	 * @param obj
	 * @return
	 */
	private String buildXtdtDetail(final HttpServletRequest request,ProceedingJoinPoint joinPoint, Object obj) {
		StringBuffer sb = new StringBuffer();
		sb.append("调用目标："+joinPoint.getSignature().getName());
		sb.append("调用URL:"+request.getRequestURL());
		sb.append("参数：\r\n");
		
		Enumeration<String> enums = request.getParameterNames();
		int i = 0;
		while (enums.hasMoreElements()) {
			String paramName = enums.nextElement();
			i++;
			sb.append("param " + i + ":" 
			+ request.getParameter(paramName) + "\r\n");
		}
		if(obj != null){
			if(obj instanceof Exception){
				sb.append("出现异常：\r\n");
				Exception ex = (Exception) obj;
	        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        	PrintStream ps = new PrintStream(baos);
	        	ex.printStackTrace(ps);
	        	ex.printStackTrace();
	        	try {
					String exMessage = baos.toString("utf-8");
					sb.append(exMessage);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				String result = null;
				
				if (obj instanceof Map) {
					result = JSONUtil.mapToJson((Map) obj);
				} else {
					result = obj.toString();
				}
				sb.append("返回值：" + result);
			}
		}
		return sb.toString();
	}
//
//	/**
//	 * 根据环境创建一个系统动态
//	 * @param contextMap
//	 * @param formatText
//	 */
//	private Xtdt createXtdt(Map contextMap, String formatText,LogTracker lt) {
//		Xtdt xtdt = new Xtdt();
//		xtdt.setDt(new Date());
//		xtdt.setIp(contextMap.get("ip") == null ? 
//	"":contextMap.get("ip").toString());
//		SecurityUser su = (SecurityUser) contextMap.get("currentUser");
//		xtdt.setTenantUn(su.getTenantUn());
//		xtdt.setOperateType(lt.opType().getValue());
//		xtdt.setTitle(formatText);
//		PersonManager personManager = (PersonManager) 
//	SpringContextUtil.getBean("personManager");
//		Person person = personManager.findById(su.getId());
//		xtdt.setPerson(person);
//		xtdt.setTenant(person.getTenant());
//		xtdt.setTenantUn(person.getTenant().getTenantUn());
//		return xtdt;
//	}

	/**
	 * 获取参数中的http request对象.
	 * @param joinPoint
	 *   aop对象
	 * @return
	 *   返回对应的request对象
	 */
	private HttpServletRequest getHttpRequest(
			final ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			if (arg instanceof HttpServletRequest) {
				return (HttpServletRequest) arg;
			}
		}
		return null;
	}
}
