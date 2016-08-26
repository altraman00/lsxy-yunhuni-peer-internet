package com.lsxy.yunhuni.file.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import com.lsxy.yunhuni.file.dao.VoiceFilePlayDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 放音文件
 * Created by zhangxb on 2016/7/21.
 */
@Service
public class VoiceFilePlayServiceImpl extends AbstractService<VoiceFilePlay> implements VoiceFilePlayService{
    @Autowired
    private VoiceFilePlayDao voiceFilePlayDao;
    @Override
    public BaseDaoInterface<VoiceFilePlay, Serializable> getDao() {
        return voiceFilePlayDao;
    }

    @Override
    public Page<VoiceFilePlay> pageList(Integer pageNo, Integer pageSize, String name,String appId,String[] tenantId,Integer status,String startTime,String endTime) {
        String hql = " from VoiceFilePlay obj  ";
        boolean isWhere = true;
        if(StringUtils.isNotEmpty(name)){
            if(isWhere){
                hql += " where ";
                isWhere = false;
            }else{
                hql +=  " and ";
            }
            hql+=" obj.name like '%"+name+"%' ";
        }
        if(StringUtil.isNotEmpty(appId)){
            if(isWhere){
                hql += " where ";
                isWhere = false;
            }else{
                hql +=  " and ";
            }
            hql+=" obj.app.id= '"+appId+"' ";
        }
        if(tenantId!=null&& tenantId.length>0){
            if(isWhere){
                hql += " where ";
                isWhere = false;
            }else{
                hql +=  " and ";
            }
            String tenantIds = "";
            for(int i=0;i<tenantId.length;i++){
                tenantIds += " '"+tenantId[i]+"' ";
                if(i!=(tenantId.length-1)){
                    tenantIds+=",";
                }
            }
            hql+=" obj.tenant.id in("+tenantIds+") ";
        }
        if(status!=null){
            if(isWhere){
                hql += " where ";
                isWhere = false;
            }else{
                hql +=  " and ";
            }
            hql+=" obj.status=  '"+status+"' ";
        }
        Date date1 = null;
        if(StringUtil.isNotEmpty(startTime)){
            try{
                date1 = DateUtils.parseDate(startTime,"yyyy-MM-dd");
            }catch (Exception e){}
        }
        Date date2 = null;
        if(StringUtil.isNotEmpty(endTime)){
            try{
                date2 = DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss");
            }catch (Exception e){}
        }
        int dateNum = 1;
        if(date1!=null){
            if(isWhere){
                hql += " where ";
                isWhere = false;
            }else{
                hql +=  " and ";
            }
            hql += " obj.createTime>=?"+dateNum;
            dateNum++;
        }
        if(date2!=null){
            if(isWhere){
                hql += " where ";
                isWhere = false;
            }else{
                hql +=  " and ";
            }
            hql += " obj.createTime<=?"+dateNum;
            dateNum++;
        }
        Page<VoiceFilePlay> page =null;
        if(date1!=null&&date2!=null){
            page  = this.pageList(hql,pageNo,pageSize,date1,date2);
        }else if(date1!=null){
            page = this.pageList(hql,pageNo,pageSize,date1);
        }else if(date2!=null){
            page = this.pageList(hql,pageNo,pageSize,date2);
        }else{
            page = this.pageList(hql,pageNo,pageSize);
        }
        return page;
    }
}
