package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.resourceTelenum.dao.ResourceTelenumDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.JdbcTemplate;
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
    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
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
    public Page getPageByFreeNumber(Integer pageNo, Integer pageSize, String telnum, String type, String areaCode, String order) {
        String hql = "  FROM ResourceTelenum obj WHERE obj.status = '"+ResourceTelenum.STATUS_FREE+"' ";
        if(StringUtils.isNotEmpty(type)){
            if("callin".equals(type)){
                hql += " AND obj.isThrough=1 ";
            }else if("callout".equals(type)){
                hql += " AND (obj.isDialing=1 or obj.isThrough=1 )";
            }
        }
        if(StringUtils.isNotEmpty(areaCode)){
            hql += " AND obj.areaCode='"+areaCode+"' ";
        }
        if(StringUtils.isNotEmpty(order)){
            if("amount:1".equals(order)){
                hql += " order by obj.amount desc ";
            }else if("amount:0".equals(order)){
                hql += " order by obj.amount ";
            }
        }else{
            hql += " order by obj.createTime desc ";
        }
        Page pgae = this.pageList(hql,pageNo,pageSize);
        return pgae;
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
        String hql = " FROM ResourceTelenum obj WHERE obj.deleted=0 ";
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
        String sql = "FROM db_lsxy_bi_yunhuni.tb_oc_resource_telenum  where deleted=0 And usable='1' AND area_code='"+areaCode+"' AND tel_number NOT IN (SELECT tel_number FROM db_lsxy_bi_yunhuni.tb_oc_telnum_to_linegateway WHERE deleted=0 AND line_id='"+id+"') ";
        if(StringUtils.isNotEmpty(operator)){
            sql +=" AND obj.operator like '%"+operator+"%' ";
        }
        if(StringUtils.isNotEmpty(number)){
            sql +=" AND obj.telNumber LIKE '%"+number+"%' ";
        }
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT * "+sql;
        Query countQuery = em.createNativeQuery(countSql);
        pageSql +=" group by create_time desc";
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
                    ResourceTelenum resultNum = null;
//                    if(appNums == null || appNums.size()==0){
//                        throw new RuntimeException("找不到对应的号码");
//                    }
                    for(ResourceTelenum num:appNums){
                        if(inFrom.equals(num.getTelNumber())){
                            resultNum = num;
                            break;
                        }
                    }
                    //找不出对应的绑定号码
//                    if(resultNum == null){
//                        throw new RuntimeException("找不到对应的号码");
//                    }else{
                        result.add(i,resultNum);
                        //下面会将这号码赋值给那些为空的号码
                        notEmptyNum = resultNum;
//                    }
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

    @Override
    public Page<Map> getTenatPageByLine(Integer pageNo, Integer pageSize, String line,String tenantName) {
        //先获取线路上的号码
        List<String> nums = telnumToLineGatewayService.getTelnumByLineId(line);
        int start = (pageNo-1)*pageSize;
        //线路上没有号码时，返回空集合
        String innums = "";
        if(nums.size()==0){
            return new Page<>(start,0,pageSize,null);
        }else{
            for(int i=0;i<nums.size();i++){
                innums += " '"+nums.get(i)+"' ";
                if(i!=nums.size()-1){
                    innums += " , ";
                }
            }
        }
        String sql = " FROM (select DISTINCT obj.tenant_id FROM db_lsxy_bi_yunhuni.tb_oc_resource_telenum obj LEFT JOIN db_lsxy_base.tb_base_tenant a on a.id=obj.tenant_id WHERE obj.deleted=0 AND obj.tenant_id IS NOT NULL  AND obj.tel_number IN ("+innums+") ";
        if(StringUtils.isNotEmpty(tenantName)){
            sql += " AND  a.tenant_name LIKE '%"+tenantName+"%'";
        }
        sql += " ) b ";
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT b.tenant_id "+sql;
        int total = jdbcTemplate.queryForObject(countSql,Integer.class);
        if(total == 0){
            return new Page<>(start,total,pageSize,null);
        }
        pageSql += " limit "+start+","+pageSize+" ";
        //获取得到租户
        List<String> list = jdbcTemplate.queryForList(pageSql,String.class);
        System.out.println(list);
        String sql2 = "SELECT * FROM db_lsxy_bi_yunhuni.tb_oc_resource_telenum obj WHERE obj.deleted=0 AND tel_number IN ("+innums+") ";
        Query query2 = em.createNativeQuery(sql2,ResourceTelenum.class);
        List<ResourceTelenum> list2 = query2.getResultList();
        List<Map> result = new ArrayList<>();
        for(int j=0;j<list.size();j++) {
            Map map = null;
            List<Map> list3= new ArrayList<>();
            for (int i = 0; i < list2.size(); i++) {
                Tenant tenant = list2.get(i).getTenant();
                if(tenant!=null&&list.get(j).equals(tenant.getId())){
                    if(map==null) {
                        map = new HashMap();
                        map.put("tenantId", list2.get(i).getTenant().getId());
                        map.put("tenantName", list2.get(i).getTenant().getTenantName());
                    }
                    Map temp = new HashMap<>();
                    temp.put("numberId",list2.get(i).getId());
                    temp.put("number",list2.get(i).getTelNumber());
                    list3.add(temp);
                }
            }
            if(map!=null) {
                map.put("numbers",list3);
                result.add(map);
            }
        }
        Page page =  new Page<>(start,total,pageSize,result);
        return page;
    }

    @Override
    public void delete(String id) {
        ResourceTelenum resourceTelenum = this.findById(id);
        //删除号码
        try {
            this.delete(resourceTelenum);
        } catch (Exception e) {
            throw new RuntimeException("删除异常");
        }
        //释放号码存在的关系
        ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
        if(resourcesRent!=null&&StringUtils.isNotEmpty(resourcesRent.getId())){
            resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
            resourcesRentService.save(resourcesRent);
        }
        //删除该号码的号码线路关系
        telnumToLineGatewayService.deleteByTelnum(resourceTelenum.getTelNumber());
    }

    @Override
    public void createNum(ResourceTelenum resourceTelenum,LineGateway lineGateway,Tenant tenant) {
        //创建号码
        resourceTelenum = this.save(resourceTelenum);
        //创建线路号码关联
        if(lineGateway!=null&&StringUtils.isNotEmpty(lineGateway.getId())){
            //判断线路号码是否已关联，提示用户自己去更新；未关联，直接产生关联
            TelnumToLineGateway telnumToLineGateway = telnumToLineGatewayService.findByTelNumberAndLineId(resourceTelenum.getTelNumber(),lineGateway.getId());
            if(telnumToLineGateway!=null && StringUtils.isNotEmpty(telnumToLineGateway.getId())){
//                return RestResponse.success("创建成功,号码和线路关系已存在");
            }else{
                telnumToLineGateway = new TelnumToLineGateway(resourceTelenum.getTelNumber(), lineGateway.getId(), resourceTelenum.getIsDialing(), resourceTelenum.getIsCalled(),resourceTelenum.getIsThrough(), resourceTelenum.getType());
                telnumToLineGatewayService.save(telnumToLineGateway);
                //一条号码有且只有在一条线路上可主叫或者可被叫
                if("1".equals(telnumToLineGateway.getIsCalled())||"1".equals(telnumToLineGateway.getIsDialing())){
                    //设置归属线路
                    resourceTelenum.setLine(lineGateway);
                    this.save(resourceTelenum);
                }
            }
        }
        if(tenant!=null) {
            resourceTelenum.setTenant(tenant);//绑定租户
            resourceTelenum.setStatus(ResourceTelenum.STATUS_RENTED);//设置被租用
            this.save(resourceTelenum);
            //判断是否需要添加号码租户的关系
            ResourcesRent resourcesRent1 = new ResourcesRent(tenant,resourceTelenum,"号码资源","1",new Date(),ResourcesRent.RENT_STATUS_UNUSED);
            resourcesRentService.save(resourcesRent1);
        }else{
            resourceTelenum.setTenant(null);//没有租户
            resourceTelenum.setStatus(ResourceTelenum.STATUS_FREE);//设置没被租用
            this.save(resourceTelenum);
        }
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "entity", key = "'entity_' + #resourceTelenum.id", beforeInvocation = true)
            }
    )
    public void editNum(ResourceTelenum resourceTelenum,int tenantType,boolean isEditNum,Tenant tenant,String telnum1,String telnum12) {
        resourceTelenum = this.save(resourceTelenum);
        //只更改租户
        if(tenantType != 0&&!isEditNum){
            if(tenantType==2){//如果修改租户，需要删除号码和租户的关系
                ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
                if(resourcesRent!=null&&StringUtils.isNotEmpty(resourcesRent.getId())){//释放存在旧的关系
                    resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
                    resourcesRentService.save(resourcesRent);
                }
            }
            //新建租用关系
            ResourcesRent resourcesRent1 = new ResourcesRent(tenant,resourceTelenum,"号码资源","1",new Date(),ResourcesRent.RENT_STATUS_UNUSED);
            resourcesRentService.save(resourcesRent1);
            //修改号码租用关系
            resourceTelenum.setTenant(tenant);
            resourceTelenum.setStatus(ResourceTelenum.STATUS_RENTED);
            this.save(resourceTelenum);
        }else if(tenantType==0&& isEditNum){//只更改手机号码
            //修改号码和租户关系，更新手机号码
            ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
            if(resourcesRent!=null&&StringUtils.isNotEmpty(resourcesRent.getId())){//存在旧的关系不用释放，只修改手机号码
                resourcesRent.setResData(resourceTelenum.getTelNumber());
                resourcesRentService.save(resourcesRent);
            }
        }else if(tenantType!=0&&isEditNum){//同时修改租户和号码
            if(tenantType==2){//如果修改租户，需要删除号码和租户的关系
                ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
                if(resourcesRent!=null&&StringUtils.isNotEmpty(resourcesRent.getId())){//释放存在旧的关系
                    resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
                    resourcesRentService.save(resourcesRent);
                }
            }
            //新建租用关系
            ResourcesRent resourcesRent1 = new ResourcesRent(tenant,resourceTelenum,"号码资源","1",new Date(),ResourcesRent.RENT_STATUS_UNUSED);
            resourcesRentService.save(resourcesRent1);
            //修改号码租用关系
            resourceTelenum.setTenant(tenant);
            resourceTelenum.setStatus(ResourceTelenum.STATUS_RENTED);
            this.save(resourceTelenum);
        }
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "entity", key = "'entity_' + #id", beforeInvocation = true)
            }
    )
    public void release(String id) {
        ResourceTelenum resourceTelenum = this.findById(id);
        ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
        resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
        resourcesRentService.save(resourcesRent);
        resourceTelenum.setTenant(null);
        resourceTelenum.setStatus(0);
        this.save(resourceTelenum);
    }
}
