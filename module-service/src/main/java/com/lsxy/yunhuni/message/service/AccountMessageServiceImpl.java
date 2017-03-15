package com.lsxy.yunhuni.message.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.yunhuni.api.message.service.AccountMessageService;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.core.utils.VelocityUtils;
import com.lsxy.yunhuni.message.dao.AccountMessageDao;
import com.lsxy.yunhuni.api.config.service.GlobalConfigService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    TenantService tenantService;
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
        String sql =" INSERT INTO db_lsxy_bi_yunhuni.tb_bi_account_message(id,message_id,account_id,status,create_time,last_time,deleted,sortno,version) VALUES ";
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
    public AccountMessage sendTenantTempletMessage(String originator, String tenantId, String type) {
        AccountMessage accountMessage = null;
        Tenant tenant = tenantService.findById(tenantId);
        if(tenant!=null) {
            Map map = new HashMap();
            map.put("name", tenant.getTenantName());
            String value = VelocityUtils.getVelocityContext(type, map);
            value = value.substring(value.indexOf("\r\n")+2,value.length());
            if (StringUtil.isEmpty(originator)) {
                originator = "系统";
            }
            accountMessage = sendMessage(originator,tenant.getRegisterUserId(),"系统通知",value);
        }
        return accountMessage;
    }

    @Override
    public AccountMessage sendTempletMessage(String originator,String accountId, String type) {
        AccountMessage accountMessage = null;
        Account account = accountService.findById(accountId);
        if(account!=null) {
            Map map = new HashMap();
            map.put("name", account.getUserName());
            String value = VelocityUtils.getVelocityContext(type, map);
            value = value.substring(value.indexOf("\r\n")+2,value.length());
            if (StringUtil.isEmpty(originator)) {
                originator = "系统";
            }
            accountMessage = sendMessage(originator,account.getId(),"系统通知",value);
        }
        return accountMessage;
    }

    @Override
    public AccountMessage sendMessage(String originator,String accountId,String title,String content) {
        Account account = accountService.findById(accountId);
        AccountMessage accountMessage = null;
        if(account!=null) {
            Message message = new Message();
            message.setType(Message.MESSAGE_ACCOUNT);
            message.setTitle(title);
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

    @Override
    public void modifyMessageStatus(String accountId, Integer status,Date endTime) {
        String sql = " update db_lsxy_bi_yunhuni.tb_bi_account_message set status='"+status+"' where account_id='"+accountId+"' and deleted='0' and status<>'-1' and last_time<=?";
        jdbcTemplate.update(sql,endTime);
    }

    @Override
    public void modifyMessageStatus(String messageId, Integer status) {
        String sql = " update db_lsxy_bi_yunhuni.tb_bi_account_message set status='"+status+"' where message_id='"+messageId+"' ";
        jdbcTemplate.update(sql);
    }

    @Override
    public Long countAll(String tenantId,  Date startTime, Date endTime) {
//        Long result = 0l; and obj.status <>-1 order by status asc,create_time desc
//        String sql = "select sum(f.a) From( " +
//                "select count(1) as a from db_lsxy_bi_yunhuni.tb_bi_account_message obj  where obj.deleted=0 and obj.account_id=?  and obj.last_time BETWEEN ? and ? " +
//                "UNION " +
//                "select count(1) as a from db_lsxy_base.tb_base_message obj1 where obj1.deleted=0 and obj1.type=? and obj1.status=? and obj1.last_time BETWEEN ? and ? ) f";
//        result = jdbcTemplate.queryForObject(sql,Long.class,tenantId,startTime,endTime,Message.MESSAGE_ACTIVITY,Message.ONLINE,startTime,endTime);
        Long result = 0l;
        String hql = "select count(1) from AccountMessage obj where deleted=0 and obj.account.id=?1 and obj.status <>-1  and obj.lastTime between ?2 and ?3";
        Query query = this.getEm().createQuery(hql);
        query.setParameter(1,tenantId);
        query.setParameter(2,startTime);
        query.setParameter(3,endTime);
        Object obj = query.getSingleResult();
        if(obj instanceof Long){
            result = (Long) obj;
        }
        return result;
    }

    @Override
    public List listAll(String tenantId, Date startTime, Date endTime) {
        String hql = "from AccountMessage obj where obj.account.id=?1 and obj.lastTime between ?2 and ?3  and obj.status <>-1 order by status asc,create_time desc";
        List list= this.list(hql,tenantId,startTime,endTime);
        return list;
    }

    @Override
    public Page pageAll(String tenantId, Date startTime, Date endTime, Integer pageNo, Integer pageSize) {
        String hql = "from AccountMessage obj where obj.account.id=?1 and obj.lastTime between ?2 and ?3  and obj.status <>-1 order by status asc,create_time desc";
        Page page= this.pageList(hql,pageNo,pageSize,tenantId,startTime,endTime);
        return page;
    }

    @Override
    public Map getAwaitNum() {
        //客服中心
        String serviceHql = "  FROM Feedback obj WHERE obj.status=?1 ";
        long awaitService = this.countByCustom(serviceHql, Feedback.UNREAD);
        //财务中心
//        String invoiceHql = "  FROM InvoiceApply obj WHERE (obj.status=?1) OR (obj.status=?2  and obj.expressNo is null) ";
//        long awaitInvoice = this.countByCustom(invoiceHql,InvoiceApply.STATUS_SUBMIT,InvoiceApply.STATUS_DONE);
        String hql = " from InvoiceApply obj where obj.status=?1 ";
        long awaitInvoiceApply =  this.countByCustom(hql,InvoiceApply.STATUS_SUBMIT);//发票审核
        String hql2 = "  from InvoiceApply obj where obj.status=?1  and obj.expressNo is null ";
        long awaitInvoiceApplySend = this.countByCustom(hql2,InvoiceApply.STATUS_DONE);//发票审核发送
        long awaitInvoice = awaitInvoiceApply+awaitInvoiceApplySend;//财务中心
        //审核中心
        String demandHql1 = "  FROM Tenant obj WHERE obj.isRealAuth in('"+Tenant.AUTH_WAIT+"','"+Tenant.AUTH_ONESELF_WAIT+"','"+Tenant.AUTH_UPGRADE_WAIT+"') ";
        String demandHql2 = "  from VoiceFilePlay obj where obj.status='"+VoiceFilePlay.STATUS_WAIT+"'  ";
        String demandHql3 = "  from App obj where obj.status='"+ App.STATUS_WAIT+"'  ";
        long awaitTenant = this.countByCustom(demandHql1);//会员认证
        long awaitPlayVoiceFile = this.countByCustom(demandHql2);//放音文件
        long awaitApp = this.countByCustom(demandHql3);//应用
        long awaitDemand = awaitTenant+awaitPlayVoiceFile+awaitApp;//审核中心
        Map map = new HashMap();
        Map map1 = new HashMap();
        map.put("awaitService",awaitService);//客服中心
        map.put("awaitInvoice",awaitInvoice);//财务中心
        map.put("awaitDemand",awaitDemand);//审核中心
        map1.put("awaitService",awaitService);//客服中心
        map1.put("awaitInvoiceApply",awaitInvoiceApply);//发票审核
        map1.put("awaitInvoiceApplySend",awaitInvoiceApplySend);//发票审核发送
        map1.put("awaitTenant",awaitTenant);//会员认证
        map1.put("awaitPlayVoiceFile",awaitPlayVoiceFile);//放音文件
        map.put("son",map1);
        return map;
    }
}
