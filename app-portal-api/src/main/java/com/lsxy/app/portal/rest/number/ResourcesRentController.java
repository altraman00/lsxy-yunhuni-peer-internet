package com.lsxy.app.portal.rest.number;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.service.TelnumLocationService;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrder;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrderItem;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelenumOrderItemService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelenumOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * 呼入号码管理
 * Created by zhangxb on 2016/7/1.
 */
@RequestMapping("/rest/res_rent")
@RestController
public class ResourcesRentController extends AbstractRestController{
    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Autowired
    TelenumOrderService telenumOrderService;
    @Autowired
    TelenumOrderItemService telenumOrderItemService;
    @Autowired
    ConsumeService consumeService;
    @Autowired
    TelnumLocationService telnumLocationService;
    @Autowired
    CalBillingService calBillingService;
    /**
     * 根据省份获取城市
     * @return
     */
    @RequestMapping("/telnum/location/city/list")
    public RestResponse getTelnumLocationList(String province)   {
        List list = telnumLocationService.getCityAndAreaCode(province);
        return RestResponse.success(list);
    }
    @RequestMapping("/telnum/city")
    public RestResponse getTelnum()   {
        List list = telnumLocationService.getCityAndAreaCodeByTelenum();
        return RestResponse.success(list);
    }
    /**
     * 获取省份列表
     * @return
     */
    @RequestMapping("/telnum/location/province/list")
    public RestResponse getTelnumLocationProvinceList()   {
        List list = telnumLocationService.getProvinceList();
        return RestResponse.success(list);
    }
    /**
     * 获取租户的呼入号码分页数据
     * @param pageNo
     * @param pageSize
     * @return
     * @throws MatchMutiEntitiesException
     */
    @RequestMapping("/list")
    public RestResponse pageList(Integer pageNo, Integer pageSize)   {
        String userName = getCurrentAccountUserName();
        //获取该租户下的所有号码信息
        Page<ResourcesRent> page = resourcesRentService.pageListByTenantId(userName,pageNo,pageSize);
        return RestResponse.success(page);
    }
    /**
     * 根据id释放手机号码
     * @param id 租户号码id
     * @return
     */
    @RequestMapping("/release")
    public RestResponse release(String id)   {
        ResourcesRent resourcesRent = resourcesRentService.findById(id);
        resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
        resourcesRentService.save(resourcesRent);
        ResourceTelenum resourceTelenum =  resourcesRent.getResourceTelenum();
        resourceTelenum.setTenant(null);
        resourceTelenum.setStatus(ResourceTelenum.STATUS_FREE);
        resourceTelenumService.save(resourceTelenum);
        return RestResponse.success(resourcesRent);
    }
    @RequestMapping("/by_app/{appId}")
    public RestResponse getByAppId(@PathVariable String appId){
        ResourcesRent rent = null;
        List<ResourcesRent> rents = resourcesRentService.findByAppId(appId);
        if(rents != null && rents.size()>0){
            rent = rents.get(0);
        }
        return RestResponse.success(rent);
    }

    /**
     * 获取号码列表
     * @param pageNo
     * @param pageSize
     * @param telnum
     * @param type
     * @param areaCode
     * @param order
     * @return
     */
    @RequestMapping("/telnum/plist" )
    public RestResponse telnumPlist(Integer pageNo,Integer pageSize,String telnum,String type,String areaCode,String order) {
        Page page = resourceTelenumService.getPageByFreeNumber(pageNo,pageSize,telnum,type,areaCode,order);
        return RestResponse.success(page);
    }

    /**
     * 获取用户的号码未支付订单
     * @return
     */
    @RequestMapping("/telnum/order" )
    public RestResponse telnumDetail() {
        Tenant tenant = getCurrentAccount().getTenant();
        TelenumOrder temp = telenumOrderService.findByTenantIdAndStatus(tenant.getId(),TelenumOrder.status_await);
        Map map = new HashMap();
        if(temp!=null&&StringUtils.isNotEmpty(temp.getId())&&tenant.getId().equals(temp.getTenantId())){
            if((new Date().getTime()-temp.getDeadline().getTime())>=0){
                temp.setStatus(TelenumOrder.status_fail);
                telenumOrderService.save(temp);
            }else {
                List<TelenumOrderItem> list = telenumOrderItemService.findByTenantIdAndTelenumOrderId(tenant.getId(), temp.getId());
                map.put("order", temp);
                map.put("list", list);
            }
        }
        return RestResponse.success(map);
    }

    /**
     *  支付订单
     * @param id
     * @return
     */
    @RequestMapping("/telnum/order/play/{id}" )
    public RestResponse telnumPlay(@PathVariable String id) {
        TelenumOrder temp = telenumOrderService.findById(id);
        Tenant tenant = getCurrentAccount().getTenant();
        if(temp!=null&&StringUtils.isNotEmpty(temp.getId())&&tenant.getId().equals(temp.getTenantId())){
            if(temp.getStatus()==TelenumOrder.status_await) {
                //余额正数部分
                Billing billing = calBillingService.getCalBilling(tenant.getId());
                if(billing.getBalance().compareTo(temp.getAmount())==-1) {
                    return RestResponse.failed("-1","余额不足");
                }
                //更新记录
                temp.setStatus(TelenumOrder.Status_success);
                telenumOrderService.save(temp);
                List<TelenumOrderItem> list = telenumOrderItemService.findByTenantIdAndTelenumOrderId(tenant.getId(), temp.getId());
                for (int i = 0; i < list.size(); i++) {
                    ResourceTelenum resourceTelenum = list.get(i).getTelnum();
                    resourceTelenum.setStatus(ResourceTelenum.STATUS_RENTED);
                    resourceTelenum.setTenant(tenant);
                    resourceTelenum = resourceTelenumService.save(resourceTelenum);
                    ResourcesRent resourcesRent = new ResourcesRent();
                    resourcesRent.setTenant(tenant);
                    resourcesRent.setResourceTelenum(resourceTelenum);
                    resourcesRent.setResData(resourceTelenum.getTelNumber());
                    resourcesRent.setResName("号码资源");
                    resourcesRent.setResType("1");
                    Date date = DateUtils.getLastTimeOfMonth(new Date());
                    resourcesRent.setRentExpire(date);
                    resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_UNUSED);
                    resourcesRentService.save(resourcesRent);
                }
                //扣费
                Consume consume = new Consume(new Date(), ConsumeCode.rent_number.name(), temp.getAmount(), ConsumeCode.rent_number.getName(), "0", tenant);
                consumeService.consume(consume);
                return RestResponse.success("支付成功");
            }else{
                return RestResponse.failed("0000","订单状态不是未支付");
            }
        }else {
            return RestResponse.failed("0000", "id无对应订单");
        }
    }
    /**
     * 取消订单
    */
    @RequestMapping("/telnum/order/delete/{id}" )
    public RestResponse telnumDelete(@PathVariable String id) {
        TelenumOrder temp = telenumOrderService.findById(id);
        String tenantId = getCurrentAccount().getTenant().getId();
        if(temp!=null&&StringUtils.isNotEmpty(temp.getId())&&tenantId.equals(temp.getTenantId())){
            if(temp.getStatus()==TelenumOrder.Status_success) {
                return RestResponse.failed("0000","成功订单无法删除");
            }
            temp.setStatus(TelenumOrder.status_fail);
            telenumOrderService.save(temp);
            List<TelenumOrderItem> list =  telenumOrderItemService.findByTenantIdAndTelenumOrderId(tenantId,temp.getId());
            for(int i=0;i<list.size();i++){
                ResourceTelenum resourceTelenum = list.get(i).getTelnum();
                resourceTelenum.setStatus(ResourceTelenum.STATUS_FREE);
                resourceTelenumService.save(resourceTelenum);
            }
            return RestResponse.success("取消成功");
        }else {
            return RestResponse.failed("0000", "id无对应订单");
        }
    }

    /**
     * 创建订单
     * @return
     */
    @RequestMapping("/telnum/order/new" )
    public RestResponse telnumNew(String ids) {
        String tenantId = getCurrentAccount().getTenant().getId();
        TelenumOrder temp = telenumOrderService.findByTenantIdAndStatus(tenantId,TelenumOrder.status_await);
        if(temp!=null&&StringUtils.isNotEmpty(temp.getId())){
            return RestResponse.failed("0000","存在未支付订单");
        }
        String[] numIds = ids.split(",");
        for(int i=0;i<numIds.length;i++){
            ResourceTelenum resourceTelenum = resourceTelenumService.findById(numIds[i]);
            if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
                return RestResponse.failed("0000","订单中有号码不存在");
            }
            if(resourceTelenum.getStatus()==ResourceTelenum.STATUS_FREE) {
            }else{
                return RestResponse.failed("0000","订单中有号码不存在");
            }
        }
        TelenumOrder telenumOrder = new TelenumOrder();
        telenumOrder.setTenantId(tenantId);
        telenumOrder.setStatus(TelenumOrder.status_await);
        telenumOrder = telenumOrderService.save(telenumOrder);
        List<String> list = new ArrayList();
        BigDecimal bigDecimal = new BigDecimal(0);
        for(int i=0;i<numIds.length;i++){
            ResourceTelenum resourceTelenum = resourceTelenumService.findById(numIds[i]);
            if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
                telenumOrder.setStatus(TelenumOrder.status_fail);
                return RestResponse.failed("0000","订单中有号码不存在");
            }
            if(resourceTelenum.getStatus()==ResourceTelenum.STATUS_FREE) {
                TelenumOrderItem telenumOrderItem = new TelenumOrderItem();
                telenumOrderItem.setTenantId(tenantId);
                telenumOrderItem.setAmount(resourceTelenum.getAmount());
                telenumOrderItem.setTelnumOrderId(telenumOrder.getId());
                telenumOrderItem.setTelnum(resourceTelenum);
                telenumOrderItemService.save(telenumOrderItem);
                list.add(numIds[i]);
                bigDecimal=bigDecimal.add(resourceTelenum.getAmount());
            }else{
                telenumOrder.setStatus(TelenumOrder.status_fail);
                clear(list);
                return RestResponse.failed("0000","订单中有号码不存在");
            }
        }
        telenumOrder.setAmount(bigDecimal);
        Calendar c = Calendar.getInstance();
        telenumOrder.setCreateTime(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, 1);
        telenumOrder.setDeadline(c.getTime());
        telenumOrder = telenumOrderService.save(telenumOrder);
        return RestResponse.success(telenumOrder);
    }
    //释放号码
    private void clear(List<String> list){
        for(int i=0;i<list.size();i++){
            ResourceTelenum resourceTelenum = resourceTelenumService.findById(list.get(i));
            resourceTelenum.setStatus(ResourceTelenum.STATUS_FREE);
            resourceTelenumService.save(resourceTelenum);
        }
    }
}
