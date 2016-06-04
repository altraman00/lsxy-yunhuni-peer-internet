package com.hesyun.framework.state.service;



import com.lsxy.framework.core.BaseService;
import com.hesyun.framework.state.model.AccountToDo;
import com.hesyun.framework.tenant.model.Account;

/**
 * 用户待办
 * @author tandy
 *
 */
public interface AccountToDoService extends BaseService<AccountToDo>{

	/**
	 * 根据ID取得对象
	 * @param personId
	 * @return
	 */
	public AccountToDo findByPersonId(String personId);
	
	
	/**
	 * 插入待办信息
	 * @param person
	 */
	public void insertToDoList(Account person);
}
