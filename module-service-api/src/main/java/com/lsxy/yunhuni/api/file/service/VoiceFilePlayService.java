package com.lsxy.yunhuni.api.file.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;

import java.util.List;

/**
 * 放音文件
 * Created by zhangxb on 2016/7/21.
 */
public interface VoiceFilePlayService extends BaseService<VoiceFilePlay> {
    /**
     * 根据文件名查找文件分页数据
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param name 文件名字
     * @return
     */
    public Page<VoiceFilePlay> pageList(Integer pageNo,Integer pageSize,String name,String appId,String[] tenantId,Integer status,String startTime,String endTime);

    /**
     * 根据文件名获取应用已审核的文件
     * @param appId
     * @param name
     * @return
     */
    public String getVerifiedFile(String appId,String name);

    /**
     * 根据未同步和同步失败的记录
     * @return
     */
    List<VoiceFilePlay> findNotSync();

    /**
     * 批量更新
     * @param ids 记录id
     * @param sync 同步状态
     */
    void batchUpdateSync(List<String> ids,Integer sync);
}
