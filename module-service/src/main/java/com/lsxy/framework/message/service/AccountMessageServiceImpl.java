package com.lsxy.framework.message.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.message.service.AccountMessageService;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.message.dao.AccountMessageDao;
import com.lsxy.yunhuni.api.config.model.GlobalConfig;
import com.lsxy.yunhuni.api.config.service.GlobalConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 用户消息实现类
 * Created by zhangxb on 2016/7/4.
 */
@Service
public class AccountMessageServiceImpl extends AbstractService<AccountMessage> implements AccountMessageService{
    private static final Logger logger = LoggerFactory.getLogger(AccountMessageServiceImpl.class);
    @Autowired
    MessageService messageService;
    @Autowired
    AccountMessageDao accountMessageDao;
    @Autowired
    AccountService accountService;
    @Autowired
    GlobalConfigService configGlobalService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<AccountMessage, Serializable> getDao() {
        return accountMessageDao;
    }

    @Override
    public Page<AccountMessage> pageListByAccountId(String userName,Integer pageNo,Integer pageSize) {
        Account  account = accountService.findAccountByUserName(userName);
        String hql = "from AccountMessage obj where obj.account.id=?1 and obj.status <>-1 order by status asc,create_time desc";
        Page<AccountMessage> page =  this.pageList(hql,pageNo,pageSize,account.getId());
        return page;
    }

    @Override
    public Long count(String userName, Integer status) {
        Long result = 0l;
        Account  account = accountService.findAccountByUserName(userName);
        String hql = "select count(1) from AccountMessage obj where deleted=0 and obj.account.id=?1 and obj.status=?2 ";
        Query query = this.getEm().createQuery(hql);
        query.setParameter(1,account.getId());
        query.setParameter(2,status);
        Object obj = query.getSingleResult();
        if(obj instanceof Long){
            result = (Long) obj;
        }
        return result;
    }

    @Override
    public void insertMultiple(List<Account> list, Message message) {
        String sql =" INSERT INTO db_lsxy_base.tb_base_account_message(id,message_id,account_id,status,create_time,last_time,deleted,sortno,version) VALUES ";
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        for(int i=0;i<list.size();i++){
            if(i!=0){
                sql += " , ";
            }
            sql += " ( '"+UUIDGenerator.uuid()+"','"+message.getId()+"','"+list.get(i).getId()+"','"+AccountMessage.NOT+"','"+initDate+"','"+initDate+"','"+0+"','"+times+"','"+0+"') ";
        }
        jdbcTemplate.update(sql);
    }

    @Override
    public AccountMessage sendTempletMessage(String originator,String accountId, String type, String name) {
        GlobalConfig configGlobal = configGlobalService.findByTypeAndName(type,name);
        Account account = accountService.findById(accountId);
        String value = configGlobal.getValue().replace("*",account.getUserName());
        return sendMessage(originator,accountId,value);
    }

    @Override
    public AccountMessage sendMessage(String originator,String accountId,String content) {
        Account account = accountService.findById(accountId);
        AccountMessage accountMessage = null;
        if(account!=null) {
            Message message = new Message();
            message.setType(Message.MESSAGE_ACCOUNT);
            message.setContent(content);
            message.setName(originator);
            message = messageService.save(message);//发送消息记录
            accountMessage = new AccountMessage();
            accountMessage.setMessage(message);
            accountMessage.setAccount(account);
            accountMessage.setStatus(AccountMessage.NOT);
            accountMessage = accountMessageDao.save(accountMessage);
        }
        return accountMessage;
    }
}
