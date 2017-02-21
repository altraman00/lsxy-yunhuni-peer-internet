package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.statistics.model.SubaccountDay;
import com.lsxy.yunhuni.api.statistics.service.SubaccountDayService;
import com.lsxy.yunhuni.statistics.dao.SubaccountDayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/2/21.
 */
@Service
public class SubaccountDayServiceImpl extends AbstractService<SubaccountDay> implements SubaccountDayService {

    @Autowired
    SubaccountDayDao subaccountDayDao;

    @Override
    public BaseDaoInterface<SubaccountDay, Serializable> getDao() {
        return this.subaccountDayDao;
    }

    @Override
    public void dayStatistics(Date date){
        String dd = DateUtils.formatDate(date, "dd");
        int day = Integer.parseInt(dd);
        String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
        Date staticsDate = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
        String statisticsDateStr = DateUtils.formatDate(staticsDate);
        Date preDate = DateUtils.getPreDate(staticsDate);
        String preDateStr = DateUtils.formatDate(preDate);
        Date nextDate = DateUtils.nextDate(staticsDate);
        String nextDateStr = DateUtils.formatDate(nextDate);

        String sql = "SELECT a.tenant_id,a.app_id,a.id AS subaccount_id , IFNULL(b.among_duration,0) AS among_duration, IFNULL(b.among_amount,0) AS among_amount, '"+statisticsDateStr+"' AS dt, "+ day +" AS day , 0 AS msg_used ," +
                "IFNULL((SELECT d.voice_used FROM tb_bi_cert_subaccount_day d WHERE d.subaccount_id = a.id AND d.dt = '" + preDateStr + "'),0) + IFNULL(b.among_duration,0) AS voice_used, " +
                "IFNULL((SELECT qu.value FROM db_lsxy_bi_yunhuni.tb_bi_cert_account_quota qu WHERE qu.type='CallQuota' AND qu.cert_account_id = a.id LIMIT 1),0) AS voice_quota_value, -1 AS msg_quota_value " +
                "FROM (SELECT p.tenant_id ,s.app_id ,p.id FROM db_lsxy_bi_yunhuni.tb_bi_api_cert p INNER JOIN db_lsxy_bi_yunhuni.tb_bi_api_cert_subaccount s ON p.id = s.id WHERE p.deleted = 0) a " +
                "LEFT JOIN " +
                "(SELECT tenant_id,app_id,subaccount_id,SUM(cost_time_long) AS among_duration,SUM(cost) AS among_amount  " +
                "FROM db_lsxy_bi_yunhuni.tb_bi_voice_cdr WHERE call_end_dt >= '" + statisticsDateStr + "'  AND call_end_dt < '" + nextDateStr + "' GROUP BY tenant_id,app_id,subaccount_id) b " +
                "ON a.id = b.subaccount_id";
        Query query = getEm().createNativeQuery(sql, SubaccountDay.class);
        List<SubaccountDay> result = query.getResultList();
        //TODO 批量保存
        if(result != null){
            for(SubaccountDay dayStatics : result){
                this.save(dayStatics);
            }
        }
    }

}
