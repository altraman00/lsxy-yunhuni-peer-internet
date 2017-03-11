package com.lsxy.msg.supplier.task;

import com.lsxy.app.uusd.message.model.PaoPaoYuMassNofity;
import com.lsxy.app.uusd.message.model.base.BaseResult;
import com.lsxy.app.uusd.message.service.BatchUpdateService;
import com.lsxy.app.uusd.message.service.PaoPaoYuService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.ussd.model.MassLog;
import com.lsxy.yunhuni.api.ussd.model.PaoPaoYuMassLog;
import com.lsxy.yunhuni.api.ussd.model.UssdUser;
import com.lsxy.yunhuni.api.ussd.service.PaoPaoYuMassLogService;
import com.lsxy.yunhuni.api.ussd.service.UssdUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时检测泡泡鱼任务是否成功
 * Created by liups on 2016/7/27.
 */
@Component
public class PaoPaoYuMassTaskLogTask {
    private static final Logger logger = LoggerFactory.getLogger(PaoPaoYuMassTaskLogTask.class);
    @Autowired
    private UssdUserService ussdUserService;
    @Autowired
    private PaoPaoYuMassLogService paoPaoYuMassLogService;
    @Autowired
    private PaoPaoYuService paoPaoYuService;
    @Autowired
    private BatchUpdateService batchUpdateService;
    /**
     * 每小时的10分钟，检测截止到当前的一个星期内的检测结果
     */
    @Scheduled(cron="0 0/10 * * * ?")
    public void hour(){
        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();
        cal.add(Calendar.DATE,-7);
        Date start = cal.getTime();
        logger.info("[泡泡鱼][群发任务][检测开启]开始时间[{}]结束时间[{}]", DateUtils.formatDate(start), DateUtils.formatDate(end));
        List<UssdUser> list = ussdUserService.getList();
        for (int i = 0; i < list.size(); i++) {
            UssdUser ussdUser = list.get(i);
            int pageNo = 1;
            int pageSize = 20;
            //查看闪印群发结果
            selectPageTsak( pageNo, pageSize , start , end , ussdUser.getId() , BaseResult.SENDTYPE_TEMP_USSD , MassLog.wait );
            //查看模板短信群发结果
            selectPageTsak( pageNo, pageSize , start , end , ussdUser.getId() , BaseResult.SENDTYPE_TEMP_USSD , MassLog.wait );
        }
        logger.info("[泡泡鱼][群发任务][检测结束]");
    }
    //查询一批任务的执行情况
    private void selectPageTsak(Integer pageNo,Integer pageSize,Date start,Date end,String userId,int sendType,int state) {
        //短信
        Page<PaoPaoYuMassLog> page = paoPaoYuMassLogService.getPage(pageNo,pageSize,start,end,userId,sendType, state);
        selectListTask(page.getResult());
        while (page.getCurrentPageNo() < page.getTotalPageCount()) {
            int tempPageNo = Integer.valueOf(page.getCurrentPageNo() + "") + 1;
            page = paoPaoYuMassLogService.getPage(pageNo,pageSize,start,end,userId ,sendType, state);
            selectListTask(page.getResult());
        }
    }
    //查询一批任务的执行情况
    private void selectListTask(List<PaoPaoYuMassLog> list){
        for(int i=0;i<list.size();i++){
            selectTask(list.get(i));
        }
    }
    //更新单任务的执行情况
    private void  selectTask(PaoPaoYuMassLog paoPaoYuMassLog){
        try {
            PaoPaoYuMassNofity nofity = paoPaoYuService.getTask(paoPaoYuMassLog.getTaskId());
            //调用查询结果成功，并且任务状态是完成。
            if( PaoPaoYuMassNofity.sueccess.equals( nofity.getResultCode() ) ){
                PaoPaoYuMassNofity.Task task = nofity.getTask();
                if( PaoPaoYuMassNofity.state_sueccess == task.getState()) {//群发任务结束
                    if(task.getPendingNum() == 0){//当前没有未发送的
                        //校验数据合理性
                        if( paoPaoYuMassLog.getPendingNum() == ( task.getSendSuccNum() + task.getSendFailNum() )){
                            //更新结果
                            batchUpdateService.updatePaoPaoYuTask( paoPaoYuMassLog , task );
                        }else{
                            logger.error("[校验][泡泡鱼][群发事件结果是否合理][不合理][期待结果值："+ paoPaoYuMassLog.getPendingNum() +"][实际结果值：(succ)"+task.getSendSuccNum() +
                                    "(fail)"+task.getSendFailNum()+"(pending)"+task.getPendingNum()+"]");
                        }
                    }else{
                        //TODO 实际结果不符合正常逻辑 应当设置报警！
                        logger.error("[校验][泡泡鱼][群发事件结果是否合理][不合理][任务状态已结束但还有未发送号码(pending)"+task.getPendingNum()+"]");
                    }
                }else{
                    logger.info("检测任务尚未完成");
                }
            }
        } catch (Exception e) {
            logger.error("[泡泡鱼][群发任务]["+paoPaoYuMassLog.getTaskId()+"]校验失败,原因:",e);
        }
    }
}
