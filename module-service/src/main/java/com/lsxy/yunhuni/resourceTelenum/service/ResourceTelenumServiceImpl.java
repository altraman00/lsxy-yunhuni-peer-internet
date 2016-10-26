package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.resourceTelenum.dao.ResourceTelenumDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 全局号码资源service
 * Created by zhangxb on 2016/7/1.
 */
@Service
public class ResourceTelenumServiceImpl extends AbstractService<ResourceTelenum> implements ResourceTelenumService {
    @Autowired
    private ResourceTelenumDao resourceTelenumDao;
    @PersistenceContext
    private EntityManager em;
    @Override
    public BaseDaoInterface<ResourceTelenum, Serializable> getDao() {
        return this.resourceTelenumDao;
    }

    @Override
    public List<String> getFreeTeleNum(int count,String areaId){
        //TODO 根据区域选择空闲的号码
        List<String> result = new ArrayList<>();
        List<ResourceTelenum> telenums = resourceTelenumDao.findFirst50ByStatus(ResourceTelenum.STATUS_FREE);
        if(telenums != null && telenums.size() > 0){
            int size = telenums.size();
            if(size <= count){
                //如果号码池中的号太少，则只取出号码池中的数量的号码即可
                for(ResourceTelenum telenum:telenums){
                    result.add(telenum.getTelNumber());
                }
            }else{
                List<Integer> ranList = new ArrayList<>();
                Random random = new Random();
                for(int i = 0;i< count;i++){
                    Integer ranNum = random.nextInt(size);
                    if(ranList.contains(ranNum)){
                        i--;
                    }else{
                        ranList.add(ranNum);
                        result.add(telenums.get(ranNum).getTelNumber());
                    }
                }
            }
        }
        return result;
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
    public String findOneFreeNumber(String areaId) {
        //TODO 根据区域获取一个空闲的号码
        ResourceTelenum resourceTelenum = resourceTelenumDao.findFirstByStatus(ResourceTelenum.STATUS_FREE);
        return resourceTelenum.getTelNumber();
    }

    @Override
    public Page<ResourceTelenum> getPage(Integer pageNo, Integer pageSize, String operator, String number) {
        String hql = " FROM ResourceTelenum obj WHERE 1=1 ";
        if(StringUtils.isNotEmpty(operator)){
            hql += " AND obj.operator='"+operator+"' ";
        }
        if(StringUtils.isNotEmpty(number)){
            hql += " AND obj.number like '%"+number+"%' ";
        }
        hql+=" ORDER BY obj.createTime DESC ";
        Page page = this.pageList(hql,pageNo,pageSize);
        return page;
    }

    @Override
    public Page<ResourceTelenum> getPageByNotLine(String id, Integer pageNo, Integer pageSize, String operator, String number) {
        String sql = "FROM db_lsxy_bi_yunhuni.tb_oc_resource_telenum obj where obj.deleted=0 AND obj.tel_number NOT IN (SELECT tel_number FROM db_lsxy_bi_yunhuni.tb_oc_telnum_to_linegateway WHERE line_id='"+id+"') ";
        if(StringUtils.isNotEmpty(operator)){
            sql +=" AND obj.operator='"+operator+"' ";
        }
        if(StringUtils.isNotEmpty(number)){
            sql +=" AND obj.number LIKE '%"+operator+"%' ";
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

}
