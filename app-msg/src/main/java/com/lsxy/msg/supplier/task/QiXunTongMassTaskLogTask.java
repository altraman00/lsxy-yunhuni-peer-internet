package com.lsxy.msg.supplier.task;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.ussd.model.*;
import com.lsxy.yunhuni.api.ussd.service.QiXunTongMassLogService;
import com.lsxy.yunhuni.api.ussd.service.QiXunTongOneLogService;
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
 * 定时判断企讯通的群发任务是否完成
 * Created by zhangxb on 2017/2/9.
 */
@Component
public class QiXunTongMassTaskLogTask {
    private static final Logger logger = LoggerFactory.getLogger(QiXunTongMassTaskLogTask.class);
    @Autowired
    private UssdUserService ussdUserService;
    @Autowired
    private QiXunTongMassLogService qiXunTongMassLogService;
    @Autowired
    private QiXunTongOneLogService qiXunTongOneLogService;
    /**
     * 每10分钟，检测截止到当前的一个星期内的检测结果
     */
    @Scheduled(cron="0 0/10 * * * ?")
    public void hour(){
        logger.info("[群发任务][检测开始]");
        List<UssdUser> list = ussdUserService.getList();
        for (int i = 0; i < list.size(); i++) {
            UssdUser ussdUser = list.get(i);
            checkUserMassTask(ussdUser);
        }
        logger.info("[群发任务][检测结束]");
    }
    //检查用户下的全部任务
    private void checkUserMassTask(UssdUser ussdUser){
        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();
        cal.add(Calendar.DATE,-7);
        Date start = cal.getTime();
        logger.info("[群发任务][检测开启][用户："+ussdUser.getUserName()+"]开始时间[{}]结束时间[{}]", DateUtils.formatDate(start), DateUtils.formatDate(end));
        //获取全部群发任务
        List<QiXunTongMassLog> list = qiXunTongMassLogService.getWaitList(ussdUser.getId(),start,end);
        for (int i = 0; i < list.size(); i++) {
            checkQiXunTongMassTask(list.get(i));
        }
    }
    //检查群发任务下的全部子任务是否成功
    private void checkQiXunTongMassTask(QiXunTongMassLog qiXunTongMassLog){
        List<QiXunTongOneLog> list = qiXunTongOneLogService.getListByTaskId(qiXunTongMassLog.getTaskId());
        int succNum = 0;//成功次数
        int failNum = 0;//失败次数
        int pendingNum = 0;//待发送数
        for (int i = 0; i < list.size(); i++) {
            QiXunTongOneLog oneLog = list.get(i);
            if(OneLog.success == oneLog.getState()){
                succNum ++;
            }else if(OneLog.wait == oneLog.getState()){
                pendingNum ++;
            }else if(OneLog.fail == oneLog.getState()){
                failNum ++;
            }
        }
        if(pendingNum == 0){//没有等待的号码，即任务完成
            //检验号码数是否正确
            if( (qiXunTongMassLog.getSuccNum() +qiXunTongMassLog.getFailNum() +qiXunTongMassLog.getPendingNum()) == (succNum + pendingNum + failNum)) {
                qiXunTongMassLog.setPendingNum(pendingNum);
                qiXunTongMassLog.setFailNum(failNum);
                qiXunTongMassLog.setSuccNum(succNum);
                qiXunTongMassLog.setState(MassLog.success);
                qiXunTongMassLogService.save(qiXunTongMassLog);
            }else{
                logger.error("[企讯通群发任务][任务id]["+qiXunTongMassLog.getTaskId()+"]参数检验失败：[新数据][succNum="+succNum+" ;pendingNum="+pendingNum+" ;failNum="+failNum+"][旧数据][" +
                        "succNum="+qiXunTongMassLog.getSuccNum() +" ;pendingNum="+qiXunTongMassLog.getPendingNum()+" ;failNum="+qiXunTongMassLog.getFailNum()+"]");
            }
        }else{
            if( (qiXunTongMassLog.getSuccNum() +qiXunTongMassLog.getFailNum() +qiXunTongMassLog.getPendingNum()) == (succNum + pendingNum + failNum)) {
                if(succNum > 0){//有号码成功即是开始处理
                    qiXunTongMassLog.setPendingNum(pendingNum);
                    qiXunTongMassLog.setFailNum(failNum);
                    qiXunTongMassLog.setSuccNum(succNum);
                    qiXunTongMassLog.setState( MassLog.beging );
                    qiXunTongMassLogService.save(qiXunTongMassLog);
                }
            }else{
                logger.error("[企讯通群发任务][任务id]["+qiXunTongMassLog.getTaskId()+"]参数检验失败：[新数据][succNum="+succNum+" ;pendingNum="+pendingNum+" ;failNum="+failNum+"][旧数据][" +
                        "succNum="+qiXunTongMassLog.getSuccNum() +" ;pendingNum="+qiXunTongMassLog.getPendingNum()+" ;failNum="+qiXunTongMassLog.getFailNum()+"]");
            }
        }
    }
}
