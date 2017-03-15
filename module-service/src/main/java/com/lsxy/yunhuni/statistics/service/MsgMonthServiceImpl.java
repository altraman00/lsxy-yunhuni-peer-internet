package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.statistics.model.MsgMonth;
import com.lsxy.yunhuni.api.statistics.model.SubaccountMonth;
import com.lsxy.yunhuni.api.statistics.service.MsgMonthService;
import com.lsxy.yunhuni.statistics.dao.MsgMonthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liups on 2017/3/14.
 */
@Service
public class MsgMonthServiceImpl extends AbstractService<MsgMonth> implements MsgMonthService {
    @Autowired
    MsgMonthDao msgMonthDao;
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
        String preDateStr = DateUtils.formatDate(preDate);
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
        String[] groups = {"tenant_id,app_id,subaccount_id","tenant_id,app_id","tenant_id"};
        String[] wheres = {};


    }

}
