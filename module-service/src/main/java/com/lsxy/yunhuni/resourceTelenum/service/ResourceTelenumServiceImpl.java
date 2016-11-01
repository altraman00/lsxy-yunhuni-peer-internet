package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.resourceTelenum.dao.ResourceTelenumDao;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

/**
 * 全局号码资源service
 * Created by zhangxb on 2016/7/1.
 */
@Service
public class ResourceTelenumServiceImpl extends AbstractService<ResourceTelenum> implements ResourceTelenumService {
    public static String testCallNumber = SystemConfig.getProperty("portal.test.call.number");
    @Autowired
    private ResourceTelenumDao resourceTelenumDao;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    ResourcesRentService resourcesRentService;

    @Override
    public BaseDaoInterface<ResourceTelenum, Serializable> getDao() {
        return this.resourceTelenumDao;
    }


    @Override
    public ResourceTelenum findByTelNumber(String telNumber) {
        return resourceTelenumDao.findByTelNumber(telNumber);
    }

    @Override
    public void cleanExpireResourceTelnum(Date expireTime) {
        resourceTelenumDao.cleanExpireResourceTelnum(expireTime);
    }

    @Override
    public ResourceTelenum findOneFreeDialingNumber(List<String> lineIds) {
        //TODO 根据线路获取一个空闲的号码
        Long numCount = resourceTelenumDao.countFreeNumber(testCallNumber,lineIds);
        Long random = RandomUtils.nextLong(0,numCount);

        ResourceTelenum resourceTelenum = resourceTelenumDao.findOneFreeNumber(testCallNumber,lineIds,random);
        return resourceTelenum;
    }

    @Override
    public String findNumByCallUri(String uri) {
        ResourceTelenum telenum = resourceTelenumDao.findByCallUri(uri);
        if(telenum != null && ResourceTelenum.USABLE_TRUE.equals(telenum.getUsable())){
            return telenum.getTelNumber();
        }else{
            return null;
        }
    }


    @Override
    public Page<ResourceTelenum> getPage(Integer pageNo, Integer pageSize, String number,String operator,String isThrough,String status) {
        String hql = " FROM ResourceTelenum obj WHERE 1=1 ";
        if(StringUtils.isNotEmpty(operator)){
            hql += " AND obj.operator like '%"+operator+"%' ";
        }
        if(StringUtils.isNotEmpty(isThrough)){
            hql += " AND obj.isThrough='"+isThrough+"' ";
        }
        if(StringUtils.isNotEmpty(status)){
            hql += " AND obj.usable='"+status+"' ";
        }
        if(StringUtils.isNotEmpty(number)){
            hql += " AND obj.telNumber like '%"+number+"%' ";
        }
        hql+=" ORDER BY obj.createTime DESC ";
        Page page = this.pageList(hql,pageNo,pageSize);
        return page;
    }

    @Override
    public Page<ResourceTelenum> getPageByNotLine(String id,String areaCode, Integer pageNo, Integer pageSize, String operator, String number) {
        String sql = "FROM db_lsxy_bi_yunhuni.tb_oc_resource_telenum obj where obj.deleted=0 AND area_code='"+areaCode+"' AND obj.tel_number NOT IN (SELECT tel_number FROM db_lsxy_bi_yunhuni.tb_oc_telnum_to_linegateway WHERE line_id='"+id+"') ";
        if(StringUtils.isNotEmpty(operator)){
            sql +=" AND obj.operator like '%"+operator+"%' ";
        }
        if(StringUtils.isNotEmpty(number)){
            sql +=" AND obj.telNumber LIKE '%"+number+"%' ";
        }
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT * "+sql;
        Query countQuery = em.createNativeQuery(countSql);
        pageSql +=" group by obj.create_time desc";
        Query pageQuery = em.createNativeQuery(pageSql,ResourceTelenum.class);
        int total = ((BigInteger)countQuery.getSingleResult()).intValue();
        int start = (pageNo-1)*pageSize;
        if(total == 0){
            return new Page<>(start,total,pageSize,null);
        }
        pageQuery.setMaxResults(pageSize);
        pageQuery.setFirstResult(start);
        List list = pageQuery.getResultList();
        return new Page<>(start,total,pageSize,list);
    }

    @Override
    public List<ResourceTelenum> findDialingTelnumber(List<String> lineIds, App app, String... from) {
        List<ResourceTelenum> result = new ArrayList<>();
        List<ResourceTelenum> appNums = new LinkedList<>();
        List<ResourcesRent> resourcesRents = resourcesRentService.findByTenantId(app.getTenant().getId());
        if(resourcesRents != null && resourcesRents.size() != 0){
            //查找应用绑定的号码
            for(ResourcesRent resourcesRent:resourcesRents){
                if(resourcesRent.getApp() != null && resourcesRent.getApp().getId().equals(app.getId())){
                    ResourceTelenum resourceTelenum = resourcesRent.getResourceTelenum();
                    if(resourceTelenum != null){
                        // 判断是否是可呼出号码
                        if("1".equals(resourceTelenum.getUsable())&& ("1".equals(resourceTelenum.getIsDialing()) || "1".equals(resourceTelenum.getIsThrough()))){
                            appNums.add(resourceTelenum);
                        }
                    }
                }
            }
            //应用没绑定号码，选择租户同一区域的号码（没被其他应用绑定）
            if(appNums.size()==0){
                for(ResourcesRent resourcesRent:resourcesRents){
                    if(resourcesRent.getApp() == null){
                        ResourceTelenum resourceTelenum = resourcesRent.getResourceTelenum();
                        if(resourceTelenum != null ){
                            // 判断是否是同一个区域
                            //判断是否是可呼出号码
                            if("1".equals(resourceTelenum.getUsable())&& ("1".equals(resourceTelenum.getIsDialing()) || "1".equals(resourceTelenum.getIsThrough()))
                                    && resourceTelenum.getAreaId().equals(app.getArea().getId())){
                                appNums.add(resourceTelenum);
                            }
                        }
                    }
                }
            }
        }
        ResourceTelenum notEmptyNum = null;
        //传入的参数大于0，则说明选号可能有多个，或者是指定了号码
        if(from.length > 0){
            for(int i=0;i<from.length;i++){
                String inFrom = from[i];
                //先给指定的号码选出数据库中对应的数据
                if(StringUtils.isNotBlank(inFrom)){
                    //号码不为空，先看应用有没有绑定号码
                    if(appNums == null || appNums.size()==0){
                        throw new RuntimeException("找不到对应的号码");
                    }
                    ResourceTelenum resultNum = null;
                    for(ResourceTelenum num:appNums){
                        if(inFrom.equals(num.getTelNumber())){
                            resultNum = num;
                            break;
                        }
                    }
                    //找不出对应的绑定号码
                    if(resultNum == null){
                        throw new RuntimeException("找不到对应的号码");
                    }else{
                        result.add(i,resultNum);
                        //下面会将这号码赋值给那些为空的号码
                        notEmptyNum = resultNum;
                    }
                }else{
                    //没指定号码直接设为空
                    result.add(i,null);
                }
            }
        }else{
            //没传from则说明只要一个号码，并且没有指定值
            result.add(null);
        }
        //如果notEmptyNum值为空，则将其赋值
        if(notEmptyNum == null){
            if(appNums == null || appNums.size()==0){
                //应用或租户没有绑定的号码，随便给一个可用的
                notEmptyNum = this.findOneFreeDialingNumber(lineIds);
            }else{
                //应用有绑定的号码，随机给一个绑定可用的
                int anInt = RandomUtils.nextInt(0, appNums.size());
                notEmptyNum = appNums.get(anInt);
            }
        }
        //将返回值列表中为空的设为可用的值
        for(int i=0;i<result.size();i++){
            if(result.get(i) == null){
                result.set(i,notEmptyNum);
            }
        }

        return result;
    }


}
