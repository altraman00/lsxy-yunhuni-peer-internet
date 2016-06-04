package com.hesyun.framework.state.service;


import java.util.Map;

import com.lsxy.framework.core.BaseService;
import com.hesyun.framework.state.model.OperationEnum;
import com.hesyun.framework.state.model.Xtdt;
import com.hesyun.framework.tenant.model.Account;

/**
 * 用户同步日志
 *
 * @author lijin
 */
@SuppressWarnings("rawtypes")
public interface XtdtService extends BaseService<Xtdt> {

	/**
	 * 统计集团用户统计
	 *
	 * @return
	 */
	String blocUserStatistics();
	
	/**
	 * 有限公司用户统计
	 *
	 * @return
	 */
	String getblocUser();
	
	/**
	 * 股份公司用户统计
	 *
	 * @return
	 */
	String stockUserStatistics();
	
	
	/**
	 * 插入账户协调系统动态数据
	 *
	 * @param p
	 * @param operateType
	 */
	
	void insertConcertXtdt(Account p, OperationEnum operateType, String busiName, String sysNames);

	/**
	 * 插入系统动态
	 *
	 * @param contextMap
	 * @param  oe
	 */
	Xtdt insertXtdt(Map contextMap, OperationEnum oe);
	
	/**
	 * 插入系统动态
	 *
	 * @param contextMap
	 * @param oe
	 */
	
	Xtdt insertXtdt(Map contextMap, OperationEnum oe, String value);
	
	/**
	 * 插入系统动态
	 *
	 * @param contextMap
	 * @param oe
	 * @param value
	 * @param person
	 * @return
	 */
	Xtdt insertXtdt(Map contextMap, OperationEnum oe, String value, Account person);
	
	/**
	 * 根据租户插入动态
	 *
	 * @param contextMap
	 * @param oe
	 * @param value
	 * @return
	 */
	Xtdt insertXtdtByTenant(Map contextMap, OperationEnum oe, String value, String tenantUn);
	
	void staffCheckInOutXtdt(Account person, OperationEnum oe);

	void customerSessionXtdt(Account person, String customerName, String customerId, OperationEnum oe);
}
