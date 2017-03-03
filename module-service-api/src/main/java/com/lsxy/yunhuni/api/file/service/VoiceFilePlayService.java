package com.lsxy.yunhuni.api.file.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;

import java.util.List;
import java.util.Map;

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
    public Page<VoiceFilePlay> pageList(Integer pageNo,Integer pageSize,String name,String appId,String[] tenantId,Integer status,String startTime,String endTime,String subId);

    /**
     * 根据文件名获取应用已审核的文件
     * @param appId
     * @param name
     * @return
     */
    public String getVerifiedFile(String appId,String name);
    /**
     * 获取未同步和同步失败的记录
     * @return
     */
    List<String> findNotSyncApp();
    /**
     * 获取未同步和同步失败的记录
     * @return
     */
    List<VoiceFilePlay> findNotSyncByApp(String app);
    /**
     * 根据appId查找对应的文件
     * @return
     */
    List<VoiceFilePlay> findByAppId(String appId);
    /**
     * 批量更新
     * @param ids 记录id
     * @param sync 同步状态
     */
    void batchUpdateSync(List<String> ids,Integer sync);
    /**
     * 批量根据key更新value
     * @param ids 记录id
     * @param key 更新的字段
     * @param value 更新的结果
     */
    void batchUpdateValueByKey(List<String> ids,String key,Object value);

    /**
     * 根据appid更新deleted字段
     * @param appId
     */
    void updateDeletedByAppId(String appId);

    void updateDeletedStautsByAppId(String appId,Object status);

    void updateDeletedStautsByid(String id,Object status);
    /**
     * 查找文件名在应用中是否已存在
     * @param tenantId
     * @param appId
     * @param name
     * @return
     */
    List<VoiceFilePlay> findByFileName(String tenantId, String appId, String name,String subId);

    /**
     * 将同步状态更新为
     * @param appId
     */
    void renewSyncByAppId(String appId);
    /** 获取已删除记录但没有删除oss文件的记录*/
    List<Map> getOssListByDeleted();
}
