package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.RealnamePrivateService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.tenant.dao.RealnamePrivateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangxb on 2016/6/29.
 * 租户实现类
 */
@Service
public class RealnamePrivateServiceImpl extends AbstractService<RealnamePrivate> implements RealnamePrivateService {
    private static final Logger logger = LoggerFactory.getLogger(RealnameRocpServiceImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RealnamePrivateDao realnamePrivaateDao;
    @Autowired
    private EntityManager em;
    @Override
    public BaseDaoInterface<RealnamePrivate, Serializable> getDao() {
        return realnamePrivaateDao;
    }


    @Override
    public List<RealnamePrivate> findByTenantId(String tenantId) {
        try {
            return  realnamePrivaateDao.findByTenantId(tenantId);
        }catch(Exception e){
            logger.error("RealnamePrivateServiceImpl.findByTenantId:{}",e);
            return null;
        }
    }

    @Override
    public List<RealnamePrivate> list(String tenantId, int status) {
        String hql = "FROM RealnamePrivate obj WHERE obj.tenant.id=?1 and obj.status=?2 ORDER BY obj.lastTime  DESC";
        List<RealnamePrivate> list = this.findByCustomWithParams(hql, tenantId,status);
        return list;
    }

    @Override
    public RealnamePrivate findByTenantIdAndStatus(String tenantId, int status) {
        try {
            return  realnamePrivaateDao.findByTenantIdAndStatus(tenantId,status);
        }catch(Exception e){
            logger.error("RealnamePrivateServiceImpl.findByTenantIdAndStatus:{}",e);
            return null;
        }
    }

    @Override
    public Page pageListAuthInfo(Integer authStatus, String startTime, String endTime, Integer type, String search, Integer pageNo, Integer pageSize) {
        String pageSql  = " SELECT e.id as uid,e.create_time as date,e.tenant_name as name ,e.tenant_id as tenantId,e.email as email,e.mobile as phone, e.type as type FROM ( ";
        String countSql = " SELECT COUNT(1) FROM ( ";
        String corp = " (SELECT c.id,c.create_time,c.tenant_name,c.tenant_id,d.email,d.mobile, 1 as type FROM (SELECT a.id,a.create_time,a.tenant_id,b.tenant_name,b.reg_user_id from  db_lsxy_base.tb_base_realname_corp  a LEFT JOIN tb_base_tenant b on a.tenant_id = b.id where a.deleted=0 ";
        String oneself = " (SELECT c.id,c.create_time,c.tenant_name,c.tenant_id,d.email,d.mobile, 0 as type FROM (SELECT a.id,a.create_time,a.tenant_id,b.tenant_name,b.reg_user_id from  db_lsxy_base.tb_base_realname_private  a LEFT JOIN tb_base_tenant b on a.tenant_id = b.id where a.deleted=0";
        if(authStatus==1){
            corp+= " and a.status='"+2+"' ";
            oneself+= " and a.status='"+authStatus+"' ";
        }else if(authStatus==-1){
            corp+= " and a.status='"+-2+"' ";
            oneself+= " and a.status='"+authStatus+"' ";
        }else{
            corp+= " and a.status='"+authStatus+"' ";
            oneself+= " and a.status='"+authStatus+"' ";
        }
        if(StringUtil.isNotEmpty(search)){
            corp+= " and b.tenant_name like ('%"+search+"%') ";
            oneself+= " and b.tenant_name like ('%"+search+"%') ";
        }
        Timestamp date1 =null;
        if(StringUtil.isNotEmpty(startTime)){
            try {
                date1 = new Timestamp(DateUtils.parseDate(startTime, "yyyy-MM-dd").getTime());
                corp += " and a.create_time>=:date1 ";
                oneself += " and a.create_time>=:date11 ";
            }catch (Exception e){

            }
        }
        Timestamp date2 = null;
        if(StringUtil.isNotEmpty(endTime)) {
            try {
                date2 = new Timestamp(DateUtils.parseDate(endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime());
                corp += " and a.create_time<=:date2 ";
                oneself += " and a.create_time<=:date22 ";
            }catch (Exception e){

            }
        }
        String three = " )c LEFT JOIN tb_base_account d ON c.reg_user_id=d.id ) ";
        String and = " UNION ";
        String end = " ) e ORDER BY e.create_time DESC ";
        if(type==null||StringUtil.isEmpty(type+"")){//查询全部
            countSql +=   corp  + three + and + oneself + three + end;
            pageSql +=   corp + three+ and + oneself + three + end;
        }else{//个人或企业认证查询
            if(Tenant.AUTH_COMPANY==type){//公司
                countSql +=  corp +  three + end;
                pageSql += corp +  three  + end;
            }else if(Tenant.AUTH_ONESELF==type){//个人
                countSql +=  oneself + three + end;
                pageSql +=  oneself +  three  + end;
            }else{
                countSql += null;
                pageSql += null;
            }
        }
        Query countQuery = em.createNativeQuery(countSql);
        Query pageQuery = em.createNativeQuery(pageSql,"authResult");
        if(date1!=null){
            countQuery.setParameter("date1",date1);
            countQuery.setParameter("date11",date1);
            pageQuery.setParameter("date1",date1);
            pageQuery.setParameter("date11",date1);
        }
        if(date2!=null){
            countQuery.setParameter("date2",date2);
            pageQuery.setParameter("date2",date2);
            countQuery.setParameter("date22",date2);
            pageQuery.setParameter("date22",date2);
        }
        int total = ((BigInteger)countQuery.getSingleResult()).intValue();
        int start = (pageNo-1)*pageSize;
        if(total == 0){
            return new Page<>(start,total,pageSize,null);
        }
        pageQuery.setMaxResults(pageSize);
        pageQuery.setFirstResult(start);
        List list = pageQuery.getResultList();
        return new Page<>(start,total,pageSize,list);
    }
    @Override
    public RealnamePrivate findByTenantIdNewest(String tenantId) {
        String hql = "from RealnamePrivate obj where obj.tenant.id=?1 order by obj.createTime desc";
        List<RealnamePrivate> ps = this.findByCustomWithParams(hql, tenantId);
        if(ps != null && ps.size()>0){
            return ps.get(0);
        }
        return null;
    }

}
