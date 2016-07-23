package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 通话记录
 * Created by zhangxb on 2016/7/19.
 */
@RequestMapping("/rest/voice_cdr")
@RestController
public class VoiceCdrController extends AbstractRestController {
    @Autowired
    VoiceCdrService voiceCdrService;

    /**
     * 获取分页数据
     * @param pageNo 当前页
     * @param pageSize 每页记录数
     * @param type 类型
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/plist")
    public RestResponse pageList(Integer pageNo,Integer pageSize,Integer type,String time,String appId){
        Page<VoiceCdr> page = voiceCdrService.pageList(pageNo,pageSize,type,getCurrentAccount().getTenant().getId(),time,appId);
        return RestResponse.success(page);
    }

    /**
     * 根据查询类型，应用id，统计当前日期的消费额度或者验证码条数
     * @param type 类型 会话类型1.语音呼叫 2.双向回拨 3.会议 4.IVR定制服务  5.语音验证码 6.录音
     * @param time 时间格式 yyyy-MM-dd
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/sum")
    public RestResponse sumCdr(Integer type,String time,String appId){
        BigDecimal sum = voiceCdrService.sumCost(type,getCurrentAccount().getTenant().getId(),time,appId);
        return RestResponse.success(sum);
    }
}
