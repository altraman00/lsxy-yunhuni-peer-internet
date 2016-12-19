package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.statistics.model.ConsumeMonth;
import com.lsxy.yunhuni.api.statistics.service.ConsumeMonthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 消费月统计
 * Created by zhangxb on 2016/7/6.
 */
@RequestMapping("/rest/consume_month")
@RestController
public class ConsumeMonthController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(ConsumeMonthController.class);
    @Autowired
    ConsumeMonthService consumeMonthService;
    /**
     * 根据时间和应用获取列表数据
     * @param appId 应用id
     * @param startTime 时间
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(String tenantId, String appId, String type, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime,"yyyy");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2  = DateUtils.parseDate(DateUtils.getLastYearByDate(endTime)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        List<ConsumeMonth> list =  consumeMonthService.list(tenantId,appId,type,date1,date2);
        changeTypeToChinese(list);
        return RestResponse.success(list);
    }

    /**
     * 获取分页数据
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping("/page")
    public RestResponse pageList(String tenantId, String appId,String type,String startTime, String endTime,Integer pageNo,Integer pageSize){
        Date date1 = DateUtils.parseDate(startTime,"yyyy");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2  = DateUtils.parseDate(DateUtils.getLastYearByDate(endTime)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        Page<ConsumeMonth> page =  consumeMonthService.pageList(tenantId,appId,type,date1,date2,pageNo,pageSize);
        changeTypeToChinese(page.getResult());
        return RestResponse.success(page);
    }
    /**
     * 获取比较时间的分页数据
     * @param tenantId 对于租户
     * @param appId 应用id
     * @param type 消费类型
     * @param startTime 时间 yyyy-MM
     * @param endTime 结束时间 yyyy-MM
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping("/compare_page")
    public RestResponse compareStartTimeAndEndTimePageList(String tenantId, String appId,String type,String startTime, String endTime,Integer pageNo,Integer pageSize){
        Date startDate1 = DateUtils.parseDate(startTime,"yyyy-MM");
        Date endDate1 =  DateUtils.parseDate(DateUtils.getMonthLastTime(DateUtils.parseDate(startTime,"yyyy-MM")),"yyyy-MM-dd HH:mm:ss");
        Date startDate2 = DateUtils.parseDate(endTime,"yyyy-MM");
        Date endDate2 =  DateUtils.parseDate(DateUtils.getMonthLastTime(DateUtils.parseDate(endTime,"yyyy-MM")),"yyyy-MM-dd HH:mm:ss");
        Page<ConsumeMonth> page =  consumeMonthService.compareStartTimeAndEndTimePageList( tenantId,  appId, type, startDate1,  endDate1,startDate2,endDate2,pageNo, pageSize);
        changeTypeToChinese(page.getResult());
        return RestResponse.success(page);
    }
    @RequestMapping("/get")
    public RestResponse get(String appId,String month){
        if(org.apache.commons.lang.StringUtils.isBlank(month)){
            String curMonth = DateUtils.getDate("yyyy-MM");
            month = DateUtils.getPrevMonth(curMonth,"yyyy-MM");
        }
        Account account = getCurrentAccount();
        List<ConsumeMonth> consumeMonths = consumeMonthService.getConsumeMonths(account.getTenant().getId(),appId,month);
        changeTypeToChinese(consumeMonths);
        return RestResponse.success(consumeMonths);
    }

    /**
     * 将消费类型转换为中文，运用枚举
     * @param consumeMonths
     */
    private void changeTypeToChinese(List<ConsumeMonth> consumeMonths){
        for(int i = 0;i < consumeMonths.size();i++){
            ConsumeMonth consumeMonth = new ConsumeMonth();
            try {
                BeanUtils.copyProperties(consumeMonth,consumeMonths.get(i));
            } catch (Exception e) {
                logger.error("转换类属性出错",e);
            }
            String type = consumeMonth.getType();
            try{
                ConsumeCode consumeCode = ConsumeCode.valueOf(type);
                consumeMonth.setType(consumeCode.getName());
            }catch(Exception e){
//                e.printStackTrace();
                consumeMonth.setType("未知项目");
            }
            consumeMonths.set(i,consumeMonth);
        }
    }

}
