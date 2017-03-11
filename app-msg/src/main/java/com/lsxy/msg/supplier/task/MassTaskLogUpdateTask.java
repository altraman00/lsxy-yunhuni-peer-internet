package com.lsxy.msg.supplier.task;

/**
 * Created by zhangxb on 2017/2/9.
 */

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.ussd.model.MassLog;
import com.lsxy.yunhuni.api.ussd.model.PaoPaoYuMassLog;
import com.lsxy.yunhuni.api.ussd.model.QiXunTongMassLog;
import com.lsxy.yunhuni.api.ussd.model.UssdUser;
import com.lsxy.yunhuni.api.ussd.service.MassLogService;
import com.lsxy.yunhuni.api.ussd.service.PaoPaoYuMassLogService;
import com.lsxy.yunhuni.api.ussd.service.QiXunTongMassLogService;
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
 * 定时检测群发任务是否完成
 * Created by liups on 2016/7/27.
 */
@Component
public class MassTaskLogUpdateTask {
    private static final Logger logger = LoggerFactory.getLogger(MassTaskLogUpdateTask.class);
    @Autowired
    private MassLogService massLogService;
    @Autowired
    private UssdUserService ussdUserService;
    @Autowired
    private PaoPaoYuMassLogService paoPaoYuMassLogService;
    @Autowired
    private QiXunTongMassLogService qiXunTongMassLogService;
    /**
     * 每12分钟，检测截止到当前的一个星期内的检测结果
     */
    @Scheduled(cron="0 0/12 * * * ?")
    public void hour(){
        logger.info("[群发任务][检测开始]");
        List<UssdUser> list = ussdUserService.getList();
        for (int i = 0; i < list.size(); i++) {
            UssdUser ussdUser = list.get(i);
            checkUserMassTask(ussdUser);
        }
        logger.info("[群发任务][检测结束]");
    }
    //处理用户下的群发任务
    private void checkUserMassTask(UssdUser ussdUser){
        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();
        cal.add(Calendar.DATE,-7);
        Date start = cal.getTime();
        logger.info("[群发任务][检测开启][用户："+ussdUser.getUserName()+"]开始时间[{}]结束时间[{}]", DateUtils.formatDate(start), DateUtils.formatDate(end));
        //获取全部群发任务
        List<MassLog> massLogList = massLogService.getWaitList( ussdUser.getId(),start,end);
        for (int j = 0; j < massLogList.size(); j++) {
            MassLog massLog = massLogList.get(j);
            ResultCode code = getState(massLog.getMsgKey());
            if(code.getState() != massLog.getState() && ((massLog.getPendingNum()+ massLog.getFailNum()+ massLog.getSuccNum())
                    ==(code.getFailNum()+code.getState()+code.getPendingNum()) ) ) {
                massLog.setState(code.getState());
                massLog.setSuccNum(code.getSuccNum());
                massLog.setFailNum(code.getFailNum());
                massLog.setPendingNum(code.getPendingNum());
                massLogService.save(massLog);
            }else{
                //TODO 实际结果不符合正常逻辑 应当设置报警！
                logger.error("[校验][群发任务][群发事件结果是否合理][不合理][code;"+code+"][masslog:"+massLog+"]");
            }
        }
    }
    //查看是否执行完成
    private ResultCode getState(String msgKey){
        int code = 0;
        int succNum = 0;//成功次数
        int failNum = 0;//失败次数
        int pendingNum = 0;//待发送数
        //泡泡鱼的群发情况
        List<PaoPaoYuMassLog> paoPaoYuMassLogList = paoPaoYuMassLogService.findByMsgKey(msgKey);
        boolean flag1 = true;
        for (int i = 0; i < paoPaoYuMassLogList.size(); i++) {
            PaoPaoYuMassLog massLog = paoPaoYuMassLogList.get(i);
            if(MassLog.success != massLog.getState()){
                flag1 = false;
            }else{
                code++;
            }
            succNum += massLog.getSuccNum();
            failNum += massLog.getFailNum();
            pendingNum += massLog.getPendingNum();
        }
        //企讯通的群发情况
        List<QiXunTongMassLog> qiXunTongMassLogs = qiXunTongMassLogService.findByMsgKey(msgKey);
        boolean flag2 = true;
        for (int i = 0; i < qiXunTongMassLogs.size(); i++) {
            QiXunTongMassLog massLog = qiXunTongMassLogs.get(i);
            if(MassLog.success != massLog.getState()){
                flag2 = false;
            }else{
                code++;
            }
            succNum += massLog.getSuccNum();
            failNum += massLog.getFailNum();
            pendingNum += massLog.getPendingNum();
        }
        int state ;
        if(flag1&&flag2){//任务完成
            state = MassLog.success;
        }else{//任务未完成
            if(code > 0){//任务已执行
                state = MassLog.beging;
            }else{
                state = MassLog.wait;
            }
        }
        return new ResultCode(state,succNum,failNum,pendingNum);
    }
    class ResultCode{
        private int state;//0：正在发送；2：结束；7：待发送
        private int succNum;//成功次数
        private int failNum;//失败次数
        private int pendingNum;//待发送数

        public ResultCode(int state, int succNum, int failNum, int pendingNum) {
            this.state = state;
            this.succNum = succNum;
            this.failNum = failNum;
            this.pendingNum = pendingNum;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getSuccNum() {
            return succNum;
        }

        public void setSuccNum(int succNum) {
            this.succNum = succNum;
        }

        public int getFailNum() {
            return failNum;
        }

        public void setFailNum(int failNum) {
            this.failNum = failNum;
        }

        public int getPendingNum() {
            return pendingNum;
        }

        public void setPendingNum(int pendingNum) {
            this.pendingNum = pendingNum;
        }

        @Override
        public String toString() {
            return "ResultCode{" +
                    "state=" + state +
                    ", succNum=" + succNum +
                    ", failNum=" + failNum +
                    ", pendingNum=" + pendingNum +
                    '}';
        }
    }
}
