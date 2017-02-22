package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.statistics.model.SubaccountMonth;
import com.lsxy.yunhuni.api.statistics.service.SubaccountMonthService;
import com.lsxy.yunhuni.statistics.dao.SubaccountMonthDao;
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
public class SubaccountMonthServiceImpl extends AbstractService<SubaccountMonth> implements SubaccountMonthService {
    @Autowired
    SubaccountMonthDao subaccountMonthDao;
    @Override
    public BaseDaoInterface<SubaccountMonth, Serializable> getDao() {
        return this.subaccountMonthDao;
    }


    @Override
    public void monthStatistics(Date date){
        String m = DateUtils.formatDate(date, "MM");
        int month = Integer.parseInt(m);
        String monthStr = DateUtils.formatDate(date, "yyyy-MM");
        Date staticsDate = DateUtils.parseDate(monthStr, "yyyy-MM");
        String statisticsDateStr = DateUtils.formatDate(staticsDate);
        Date preDate = DateUtils.getPrevMonth(staticsDate);
        String preDateStr = DateUtils.formatDate(preDate);
        String nextDateStr = DateUtils.getNextMonth(statisticsDateStr, "yyyy-MM-dd HH:mm:ss");

        String sql = " SELECT a.tenant_id,a.app_id,a.id AS subaccount_id , IFNULL(b.among_duration,0) AS among_duration, IFNULL(b.among_amount,0) AS among_amount, '"+statisticsDateStr+"' AS dt, "+ month +" AS MONTH , 0 AS msg_used ," +
                " IFNULL((SELECT d.voice_used FROM db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_day d WHERE d.subaccount_id = a.id AND d.dt = '" + preDateStr + "'),0) + IFNULL(b.among_duration,0) AS voice_used, " +
                " IFNULL((SELECT qu.value FROM db_lsxy_bi_yunhuni.tb_bi_cert_account_quota qu WHERE qu.type='CallQuota' AND qu.cert_account_id = a.id LIMIT 1),0) AS voice_quota_value, -1 AS msg_quota_value " +
                " FROM (SELECT p.tenant_id ,s.app_id ,p.id FROM db_lsxy_bi_yunhuni.tb_bi_api_cert p INNER JOIN db_lsxy_bi_yunhuni.tb_bi_api_cert_subaccount s ON p.id = s.id WHERE p.deleted = 0) a " +
                " LEFT JOIN " +
                " ( SELECT tenant_id,app_id,subaccount_id,SUM(among_duration) AS among_duration,SUM(among_amount) AS among_amount  " +
                " FROM db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_day  WHERE dt >= '" + statisticsDateStr + "' AND  dt < '" + nextDateStr + "' GROUP BY tenant_id,app_id,subaccount_id) b " +
                " ON a.id = b.subaccount_id ";
        Query query = getEm().createNativeQuery(sql, SubaccountMonth.class);
        List<SubaccountMonth> result = query.getResultList();
        //TODO 批量保存
        if(result != null){
            for(SubaccountMonth monthStatics : result){
                this.save(monthStatics);
            }
        }
    }

}
