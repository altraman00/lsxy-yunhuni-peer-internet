package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.utils.StatisticsUtils;
import com.lsxy.yunhuni.api.statistics.model.ConsumeMonth;
import com.lsxy.yunhuni.api.statistics.model.MsgDay;
import com.lsxy.yunhuni.api.statistics.model.MsgStatisticsVo;
import com.lsxy.yunhuni.api.statistics.model.SubaccountDay;
import com.lsxy.yunhuni.api.statistics.service.MsgDayService;
import com.lsxy.yunhuni.statistics.dao.MsgDayDao;
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
public class MsgDayServiceImpl extends AbstractService<MsgDay> implements MsgDayService {
    @Autowired
    MsgDayDao msgDayDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public BaseDaoInterface<MsgDay, Serializable> getDao() {
        return this.msgDayDao;
    }

    @Override
    public void dayStatistics(Date date){
        String dd = DateUtils.formatDate(date, "dd");
        int day = Integer.parseInt(dd);
        String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
        Date staticsDate = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
        String statisticsDateStr = DateUtils.formatDate(staticsDate);
        Date preDate = DateUtils.getPreDate(staticsDate);
        Date nextDate = DateUtils.nextDate(staticsDate);
        String nextDateStr = DateUtils.formatDate(nextDate);
        String currentDateStr = DateUtils.formatDate(new Date());

        //子账号最初开始有的日期2017-03-14，之前的日期不用算了
        Date firstStatisticsDate = DateUtils.parseDate("2017-03-14", "yyyy-MM-dd");
        SubaccountDay lastStatistics = msgDayDao.findFirstByDt(preDate);
        //如果前一天没有统计数据，并且要统计的时间大于2017-03-14,则先统计前一天的数据
        if(lastStatistics == null && staticsDate.getTime() > firstStatisticsDate.getTime()) {
            dayStatistics(preDate);
        }
        //如果今天有统计数据了，则说明不用统计了
        SubaccountDay todayStatistics = msgDayDao.findFirstByDt(staticsDate);
        if(todayStatistics != null){
            return;
        }
        String[] selects = {"tenant_id,app_id,","tenant_id,",""};

        for(String select:selects){
            String sql = "SELECT REPLACE(UUID(), '-', '') AS id,"+ select +"send_type AS TYPE,COUNT(1) AS total," +
                    "IFNULL(SUM(CASE WHEN (state=1) THEN 1 ELSE 0 END),0) AS success,IFNULL(SUM(CASE WHEN (state=-1) THEN 1 ELSE 0 END),0) AS fail," +
                    "'"+statisticsDateStr+"' AS dt, "+ day +" AS day ," +
                    "'"+currentDateStr + "' AS create_time, '"+ currentDateStr + "' AS last_time," + " 0 AS deleted,0 AS sortno,0 AS version "+
                    "FROM db_lsxy_bi_yunhuni.tb_bi_msg_send_detail  WHERE end_time >= '"+statisticsDateStr+"' AND end_time < '"+ nextDateStr +"' GROUP BY "+ select +"send_type";
            Query query = getEm().createNativeQuery(sql);
            List result = query.getResultList();
            if(result != null && result.size() >0){
                String values = "id," + select + "type,total,success,fail,dt,day, create_time,last_time,deleted,sortno,version";
                String valuesMark = "";
                int length = values.split(",").length;
                for(int i = 0;i<length;i++){
                    if(i == length -1){
                        valuesMark += "?";
                    }else{
                        valuesMark += "?,";
                    }
                }
                String insertSql = "INSERT INTO db_lsxy_bi_yunhuni.tb_bi_msg_day_statistics ("+values+") values ("+valuesMark+")";
                jdbcTemplate.batchUpdate(insertSql,result);
            }
        }

    }


    @Override
    public Long getUsed(String tenantId, String appId, String subaccountId,String sendType, Date startTime, Date endTime){
        String sql1 = "SELECT COUNT(1) AS used FROM db_lsxy_bi_yunhuni.tb_bi_msg_send_detail WHERE send_type='"+sendType+"' ";
        String sql2 = "SELECT COUNT(1) AS returned FROM db_lsxy_bi_yunhuni.tb_bi_msg_send_detail WHERE send_type='"+sendType+"' AND state=-1 ";

        if(org.apache.commons.lang.StringUtils.isNotEmpty(tenantId)){
            sql1 +=" AND tenant_id='"+tenantId+"' " ;
            sql2 +=" AND tenant_id='"+tenantId+"' " ;
        }
        if(org.apache.commons.lang.StringUtils.isNotEmpty(appId)){
            sql1 += " AND app_id='"+appId+"' ";
            sql2 += " AND app_id='"+appId+"' ";
        }
        if(org.apache.commons.lang.StringUtils.isNotEmpty(subaccountId)){
            sql1 += " AND subaccount_id='"+subaccountId+"' ";
            sql2 += " AND subaccount_id='"+subaccountId+"' ";
        }
        if(startTime != null){
            String startTimeStr = DateUtils.getDate(startTime,"yyyy-MM-dd HH:mm:ss");
            sql1 += " AND create_time>='"+startTimeStr+"' " ;
            sql2 += " AND end_time>='"+startTimeStr+"' " ;
        }
        if(endTime != null){
            String endTimeStr = DateUtils.getDate(endTime,"yyyy-MM-dd HH:mm:ss");
            sql1 += " AND create_time<'"+endTimeStr+"' " ;
            sql2 += " AND end_time<'"+endTimeStr+"' " ;
        }
        Map map1 = jdbcTemplate.queryForMap(sql1);
        Map map2 = jdbcTemplate.queryForMap(sql2);
        Long used = (Long) map1.get("used");
        Long returned = (Long) map2.get("returned");
        return used - returned;
    }


    @Override
    public Page<MsgStatisticsVo> getStatisticsPage(String tenantId, String appId, Date date1, Date date2, Integer pageNo, Integer pageSize) {
        List<MsgDay> list =  getAllList(tenantId,appId,date1,date2);
        Map<Object ,MsgStatisticsVo> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            MsgDay msgDay = list.get(i);
            MsgStatisticsVo msgVo = map.get(msgDay.getDay());
            if(msgVo==null){
                msgVo = new MsgStatisticsVo(msgDay);
            }else{
                msgVo.add(msgDay);
            }
            map.put(msgDay.getDay(),msgVo);
        }
        List<MsgStatisticsVo> list2 = null;
        if(list.size()>0) {
            list2 = MsgStatisticsVo.initMsgStatisticsVos(date1, 1, map);
        }else{
            list2 = new ArrayList<>();
        }
        return new Page<>((pageNo-1)*pageSize,list.size(),pageSize,list2);
    }

    @Override
    public List<MsgStatisticsVo> getStatisticsList(String tenantId, String appId, Date date1, Date date2) {
        List<MsgDay> list =  getAllList(tenantId,appId,date1,date2);
        Map<Object ,MsgStatisticsVo> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            MsgDay msgDay = list.get(i);
            MsgStatisticsVo msgVo = map.get(msgDay.getDay());
            if(msgVo==null){
                msgVo = new MsgStatisticsVo(msgDay);
            }else{
                msgVo.add(msgDay);
            }
            map.put(msgDay.getDay(),msgVo);
        }
        return MsgStatisticsVo.initMsgStatisticsVos(date1,1,map);
    }
    private List<MsgDay> getAllList(String tenantId, String appId, Date date1, Date date2){
        String hql = " from MsgDay obj where obj.tenantId=?1 ";
        List<MsgDay> list = null;
        if(StringUtils.isNotEmpty(appId)){
            hql += " and obj.appId = ?2 and obj.dt between ?2 and ?3 ";
            list = this.list(hql,tenantId,appId,date1,date2 );
        }else{
            hql+= " and obj.dt between ?2 and ?3 ";
            list = this.list(hql,tenantId,date1,date2 );
        }
        return list;
    }
}
