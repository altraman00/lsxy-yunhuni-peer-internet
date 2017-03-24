package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.statistics.model.MsgDay;
import com.lsxy.yunhuni.api.statistics.model.MsgMonth;
import com.lsxy.yunhuni.api.statistics.model.MsgStatisticsVo;
import com.lsxy.yunhuni.api.statistics.model.SubaccountMonth;
import com.lsxy.yunhuni.api.statistics.service.MsgMonthService;
import com.lsxy.yunhuni.statistics.dao.MsgMonthDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;

/**
 * Created by liups on 2017/3/14.
 */
@Service
public class MsgMonthServiceImpl extends AbstractService<MsgMonth> implements MsgMonthService {
    @Autowired
    MsgMonthDao msgMonthDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public BaseDaoInterface<MsgMonth, Serializable> getDao() {
        return this.msgMonthDao;
    }

    @Override
    public void monthStatistics(Date date){
        String m = DateUtils.formatDate(date, "MM");
        int month = Integer.parseInt(m);
        String monthStr = DateUtils.formatDate(date, "yyyy-MM");
        Date staticsDate = DateUtils.parseDate(monthStr, "yyyy-MM");
        String statisticsDateStr = DateUtils.formatDate(staticsDate);
        Date preDate = DateUtils.getPrevMonth(staticsDate);
        String nextDateStr = DateUtils.getNextMonth(statisticsDateStr, "yyyy-MM-dd HH:mm:ss");
        String currentDateStr = DateUtils.formatDate(new Date());

        //子账号最初开始有的日期2017-03-14，之前的日期不用算了
        Date firstStatisticsDate = DateUtils.parseDate("2017-03", "yyyy-MM");
        SubaccountMonth lastStatistics = msgMonthDao.findFirstByDt(preDate);
        //如果前一天没有统计数据，并且要统计的时间大于2017-03-14,则先统计前一天的数据
        if(lastStatistics == null && staticsDate.getTime() >= firstStatisticsDate.getTime()) {
            monthStatistics(preDate);
        }
        //如果今天有统计数据了，则说明不用统计了
        SubaccountMonth todayStatistics = msgMonthDao.findFirstByDt(staticsDate);
        if(todayStatistics != null){
            return;
        }
        String[] selects = {"tenant_id,app_id,","tenant_id,",""};

        for(String select:selects){
            String sql = "SELECT REPLACE(UUID(), '-', '') AS id,"+ select +"TYPE,SUM(total) AS total,SUM(success) AS success,SUM(fail) AS fail, " +
                    "'"+statisticsDateStr+"' AS dt, "+ month +" AS month ," +
                    "'"+currentDateStr + "' AS create_time, '"+ currentDateStr + "' AS last_time," + " 0 AS deleted,0 AS sortno,0 AS version "+
                    " FROM db_lsxy_bi_yunhuni.tb_bi_msg_day_statistics WHERE tenant_id is not null and app_id is not null " +
                    " AND dt >= '"+statisticsDateStr+"' AND dt < '"+ nextDateStr +"' GROUP BY "+ select +"type";

            Query query = getEm().createNativeQuery(sql);
            List result = query.getResultList();
            if(result != null && result.size() >0) {
                String values = "id," + select + "type,total,success,fail,dt,month, create_time,last_time,deleted,sortno,version";
                String valuesMark = "";
                int length = values.split(",").length;
                for (int i = 0; i < length; i++) {
                    if (i == length - 1) {
                        valuesMark += "?";
                    } else {
                        valuesMark += "?,";
                    }
                }
                String insertSql = "INSERT INTO db_lsxy_bi_yunhuni.tb_bi_msg_month_statistics (" + values + ") values (" + valuesMark + ")";
                jdbcTemplate.batchUpdate(insertSql, result);
            }
        }

    }

    @Override
    public Page<MsgStatisticsVo> getStatisticsPage(String tenantId, String appId, Date date1, Date date2, Integer pageNo, Integer pageSize) {
        List<MsgMonth> list =  getAllList(tenantId,appId,date1,date2);
        Map<Object ,MsgStatisticsVo> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            MsgMonth msgMonth = list.get(i);
            MsgStatisticsVo msgVo = map.get(msgMonth.getMonth());
            if(msgVo==null){
                msgVo = new MsgStatisticsVo(msgMonth);
            }else{
                msgVo.add(msgMonth);
            }
            map.put(msgMonth.getMonth(),msgVo);
        }
        List<MsgStatisticsVo> list2 = null;
        if(list.size()>0) {
            list2 = MsgStatisticsVo.initMsgStatisticsVos(date1, 2, map);
        }else{
            list2 = new ArrayList<>();
        }
        return new Page<>((pageNo-1)*pageSize,list.size(),pageSize,list2);
    }

    @Override
    public List<MsgStatisticsVo> getStatisticsList(String tenantId, String appId, Date date1, Date date2) {
        List<MsgMonth> list =  getAllList(tenantId,appId,date1,date2);
        Map<Object ,MsgStatisticsVo> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            MsgMonth msgMonth = list.get(i);
            MsgStatisticsVo msgVo = map.get(msgMonth.getMonth());
            if(msgVo==null){
                msgVo = new MsgStatisticsVo(msgMonth);
            }else{
                msgVo.add(msgMonth);
            }
            map.put(msgMonth.getMonth(),msgVo);
        }
        return MsgStatisticsVo.initMsgStatisticsVos(date1,2,map);
    }
    private List<MsgMonth> getAllList(String tenantId, String appId, Date date1, Date date2){
        String hql = " from MsgMonth obj where obj.tenantId=?1 ";
        List<MsgMonth> list = null;
        if(StringUtils.isNotEmpty(appId)){
            hql += " and obj.appId = ?2 and obj.dt between ?2 and ?3 ";
            list = this.list(hql,tenantId,appId,date1,date2 );
        }else{
            hql+= " and obj.appId = null and obj.dt between ?2 and ?3 ";
            list = this.list(hql,tenantId,date1,date2 );
        }
        return list;
    }
}
