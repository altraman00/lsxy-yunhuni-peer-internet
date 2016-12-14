package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToPublicService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToTenantService;
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
import java.util.stream.Collectors;

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
    @Autowired
    private LineGatewayService lineGatewayService;
    @Autowired
    LineGatewayToTenantService lineGatewayToTenantService;
    @Autowired
    LineGatewayToPublicService lineGatewayToPublicService;

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
    public Page getFreeNumberPage(String tenantId,Integer pageNo, Integer pageSize, String telNum, String type, String areaCode, String order) {
        //查找租户私有线路
        List<LineGateway> lineGateways = lineGatewayToTenantService.findLineGatewayByTenantId(tenantId);
        if(lineGateways == null || lineGateways.size() == 0){
            //如果没有私有线路，找公共线路
            lineGateways = lineGatewayToPublicService.findAllLineGateway();
        }
        if(lineGateways == null || lineGateways.size() == 0){
            //没有线路，则抛出异常
            throw new RuntimeException("没有可用线路");
        }
        //所拥有的线路ID列表
        List<String> lineIds = lineGateways.parallelStream().map(LineGateway::getId).collect(Collectors.toList());

        String numSql = "SELECT * FROM db_lsxy_bi_yunhuni.tb_oc_resource_telenum num WHERE num.deleted=0 AND num.status = 0 AND num.usable=1 AND tel_number <> :testNum ";
        if(StringUtils.isNotEmpty(telNum)){
            numSql += " and num.tel_number = :telNum";
        }
        if(StringUtils.isNotEmpty(areaCode)){
            numSql += " AND num.area_code= :areaCode";
        }
        String ttlSql = "SELECT DISTINCT ttl.tel_number FROM db_lsxy_bi_yunhuni.tb_oc_telnum_to_linegateway ttl WHERE ttl.line_id  IN (:lineIds) AND ttl.deleted = 0 ";
        if(StringUtils.isNotEmpty(type)){
            if("callin".equals(type)){
                ttlSql += " AND ttl.is_dialing=1 ";
            }else if("callout".equals(type)){
                ttlSql += " AND (ttl.is_through=1 or ttl.is_through=1 ) ";
            }
        }
        //查询总数
        String countSql = "SELECT COUNT(1) FROM " +
                " (" + numSql +") a " +
                "  INNER JOIN " +
                "  ( " + ttlSql +") b " +
                "  ON a.tel_number = b.tel_number ";
        Query countQuery = getEm().createNativeQuery(countSql);
        countQuery.setParameter("testNum",testCallNumber);
        countQuery.setParameter("lineIds",lineIds);
        if(StringUtils.isNotEmpty(telNum)){
            countQuery.setParameter("telNum",testCallNumber);
        }
        if(StringUtils.isNotEmpty(areaCode)){
            countQuery.setParameter("areaCode",areaCode);
        }
        long total = ((BigInteger) countQuery.getSingleResult()).longValue();
        if(total == 0){
            return new Page();
        }

        //查询分页数据
        String resultSql = "SELECT * FROM " +
                " (" + numSql +") a " +
                "  INNER JOIN " +
                "  ( " + ttlSql +") b " +
                "  ON a.tel_number = b.tel_number ";
        if(StringUtils.isNotEmpty(order)){
            if("amount:1".equals(order)){
                resultSql += " order by a.amount desc ";
            }else if("amount:0".equals(order)){
                resultSql += " order by a.amount ";
            }
        }else{
            resultSql += " order by a.create_time desc ";
        }
        Query query = getEm().createNativeQuery(resultSql, ResourceTelenum.class);
        query.setParameter("testNum",testCallNumber);
        query.setParameter("lineIds",lineIds);
        if(StringUtils.isNotEmpty(telNum)){
            query.setParameter("telNum",testCallNumber);
        }
        if(StringUtils.isNotEmpty(areaCode)){
            query.setParameter("areaCode",areaCode);
        }
        int start = (pageNo - 1) * pageSize;
        query.setMaxResults(pageSize);
        query.setFirstResult(start);
        List resultList = query.getResultList();

        return new Page(start,total,pageSize,resultList);
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
        //获取绑定线路
        List<ResourceTelenum> result = page.getResult();
        Set<String> lineIds = new HashSet<>();
        for(ResourceTelenum telenum:result){
            if(StringUtils.isNotBlank(telenum.getLineId())){
                lineIds.add(telenum.getLineId());
            }
        }
        List<LineGateway> lines = lineGatewayService.findByIds(lineIds);
        Map<String,LineGateway> map = new HashMap<>();
        for(LineGateway line:lines){
            map.put(line.getId(),line);
        }
        for(ResourceTelenum telenum:result){
            telenum.setLine(map.get(telenum.getLineId()));
        }
        return page;
    }

    @Override
    public Page<ResourceTelenum> getPageByNotLine(String id,String areaCode, Integer pageNo, Integer pageSize, String operator, String number) {
        String sql = "FROM db_lsxy_bi_yunhuni.tb_oc_resource_telenum  where deleted=0  AND area_code='"+areaCode+"' AND tel_number NOT IN (SELECT tel_number FROM db_lsxy_bi_yunhuni.tb_oc_telnum_to_linegateway WHERE deleted=0 AND line_id='"+id+"') ";
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
        //from有几个，则反回的result有几个号码
        List<ResourceTelenum> result = new ArrayList<>();
        if(from != null && from.length > 0){
            List<String> notBlankFrom = new ArrayList<>();
            for(String f:from){
                if(StringUtils.isNotBlank(f)){
                    notBlankFrom.add(f);
                }
            }
            //当用户指定了号码
            if(notBlankFrom.size() > 0){
                List<String> resIds = new ArrayList<>();
                //查出用户指定的号码
                List<Object[]> rentResult = resourceTelenumDao.findResIdByTenantIdAndResDataInFromRent(app.getTenant().getId(), notBlankFrom);
                if(rentResult != null){
                    for(Object[] arr:rentResult){
                        //查出的租用信息要绑定当前应用，或都是没有应用绑定，才是有效的
                        if(arr[1] != null && (arr[0] == null || app.getId().equals(arr[0]))){
                            resIds.add(arr[1].toString());
                        }
                    }
                }
                List<ResourceTelenum> availableNums = new ArrayList<>();
                //判断用户的号码是否合格，合格则放到可用号码列表中
                if(resIds.size() == 1){
                    ResourceTelenum telnum = this.findById(resIds.get(0));
                    if(telnum != null && ResourceTelenum.USABLE_TRUE.equals(telnum.getUsable())&& (ResourceTelenum.ISDIALING_TRUE.equals(telnum.getIsDialing()) || ResourceTelenum.ISTHROUGH_TRUE.equals(telnum.getIsThrough()))
                            &&telnum.getAreaId() !=null && telnum.getAreaId().equals(app.getArea().getId())){
                        availableNums.add(telnum);
                    }
                }else if(resIds.size() > 1){
                    List<ResourceTelenum> findNums = this.findByIds(resIds);
                    if(availableNums != null){
                        for(ResourceTelenum telnum:findNums){
                            if(telnum != null && ResourceTelenum.USABLE_TRUE.equals(telnum.getUsable())&& (ResourceTelenum.ISDIALING_TRUE.equals(telnum.getIsDialing()) || ResourceTelenum.ISTHROUGH_TRUE.equals(telnum.getIsThrough()))
                                    &&telnum.getAreaId() !=null && telnum.getAreaId().equals(app.getArea().getId())){
                                availableNums.add(telnum);
                            }
                        }
                    }
                }
                //可用号码列表不为空
                if(availableNums.size() > 0){
                    for(String fr:from){
                        if(StringUtils.isBlank(fr)){
                            result.add(availableNums.get(0));
                        }else{
                            ResourceTelenum availableFrom = null;
                            for(ResourceTelenum availableNum:availableNums){
                                if(fr.equals(availableNum.getTelNumber())){
                                    availableFrom = availableNum;
                                    break;
                                }
                            }
                            if(availableFrom == null){
                                availableFrom = availableNums.get(0);
                            }
                            result.add(availableFrom);
                        }
                    }
                }
            }
        }
        //经过以上处理后，返回号码结果还是空的话，则不根据传入的from来选号码，选租户应用下的号码，或者租户下不被应用绑定的号码
        if(result.size() == 0){
            ResourceTelenum availableNum = null;
            Map<String,String> map = new HashMap<>();
            List<Object[]> arrs = resourceTelenumDao.findResIdByTenantIdAndAppIdFromRent(app.getTenant().getId(),app.getId());
            if(arrs != null){
                arrs.parallelStream().forEach(arr-> {
                    if(arr[1] != null && StringUtils.isNotBlank(arr[1].toString())){
                        map.put(arr[1].toString(),arr[0] == null?null:arr[0].toString());
                    }
                });
                Set<String> numIds = map.keySet();
                List<ResourceTelenum> telnums = this.findByIds(numIds);
                telnums.parallelStream().forEach(telnum -> telnum.setAppId(map.get(telnum.getId())));
                for(ResourceTelenum telnum:telnums){
                    if(telnum != null && telnum.getAppId() != null && telnum.getAppId().equals(app.getId())){
                        // 判断是否是可呼出号码
                        if(ResourceTelenum.USABLE_TRUE.equals(telnum.getUsable())&& (ResourceTelenum.ISDIALING_TRUE.equals(telnum.getIsDialing()) || ResourceTelenum.ISTHROUGH_TRUE.equals(telnum.getIsThrough()))){
                            availableNum = telnum;
                            break;
                        }
                    }
                }
                if(availableNum == null){
                    for(ResourceTelenum telnum:telnums){
                        if(telnum != null && telnum.getAppId() == null){
                            // 判断是否是同一个区域
                            //判断是否是可呼出号码
                            if(ResourceTelenum.USABLE_TRUE.equals(telnum.getUsable())&& (ResourceTelenum.ISDIALING_TRUE.equals(telnum.getIsDialing()) || ResourceTelenum.ISTHROUGH_TRUE.equals(telnum.getIsThrough()))
                                    && telnum.getAreaId().equals(app.getArea().getId())){
                                availableNum = telnum;
                                break;
                            }
                        }
                    }
                }
            }
            if(availableNum == null){
                availableNum = this.findOneFreeDialingNumber(lineIds);
            }
            if(from != null && from.length > 0){
                for(int i=0;i<from.length;i++){
                    result.add(availableNum);
                }
            }else{
                result.add(availableNum);
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
                if(TelnumToLineGateway.ISCALLED_TRUE.equals(telnumToLineGateway.getIsCalled())||TelnumToLineGateway.ISDIALING_TRUE.equals(telnumToLineGateway.getIsDialing())){
                    //设置归属线路
                    resourceTelenum.setLineId(lineGateway.getId());
                    this.save(resourceTelenum);
                }
            }
        }
        if(tenant!=null) {
            resourceTelenum.setTenant(tenant);//绑定租户
            resourceTelenum.setStatus(ResourceTelenum.STATUS_RENTED);//设置被租用
            this.save(resourceTelenum);
            //判断是否需要添加号码租户的关系
            ResourcesRent resourcesRent1 = new ResourcesRent(tenant,resourceTelenum,"号码资源",ResourcesRent.RESTYPE_TELENUM,new Date(),ResourcesRent.RENT_STATUS_UNUSED);
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
            ResourcesRent resourcesRent1 = new ResourcesRent(tenant,resourceTelenum,"号码资源",ResourcesRent.RESTYPE_TELENUM,new Date(),ResourcesRent.RENT_STATUS_UNUSED);
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
            ResourcesRent resourcesRent1 = new ResourcesRent(tenant,resourceTelenum,"号码资源",ResourcesRent.RESTYPE_TELENUM,new Date(),ResourcesRent.RENT_STATUS_UNUSED);
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

    @Override
    public List<ResourceTelenum> findByIds(Collection<String> ids) {
        return resourceTelenumDao.findByIdIn(ids);
    }
}
