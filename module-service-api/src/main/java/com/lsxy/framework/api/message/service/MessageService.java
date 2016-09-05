package com.lsxy.framework.api.message.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.core.utils.Page;

import java.util.Date;
import java.util.List;

/**
 * 消息service
 * Created by zhangxb on 2016/7/4.
 */
public interface MessageService extends BaseService<Message> {
    /**
     * 获取分页信息
     * @param type 状态
     * @param status 类型 0未上线 1上线 -1下线
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageNo 第几页
     * @param pageSize 每页面记录数
     * @return
     */
    Page<Message> pageList(Integer type,Integer status,String startTime,String endTime,Integer pageNo, Integer pageSize);

    /**
     * 批量更新
     * @param startTime
     * @param endTime
     */
    List<Message> bacthUpdateStatus(Date startTime, Date endTime);
}
