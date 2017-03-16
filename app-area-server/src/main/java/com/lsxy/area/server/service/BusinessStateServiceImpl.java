package com.lsxy.area.server.service;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.util.CallbackUrlUtil;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.MapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuws on 2016/8/29.
 * 目前业务都是异步的
 * 所以用redis保存业务数据状态
 */
@Component
public class BusinessStateServiceImpl implements BusinessStateService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessStateServiceImpl.class);

    @Autowired
    private RedisCacheService redisCacheService;

    private int EXPIRE_START = 60 * 60 * 12;

    private int EXPIRE_RELEASE = 60 * 30;

    private String getKey(String id){
        return "business_state_" + id;
    }

    /**为了节省redis内存使用代码替代通用信息的key**/
    private static final String ID = ("1");
    private static final String TENANTID = ("2");
    private static final String APPID = ("3");
    private static final String SUBACCOUNTID = ("4");
    private static final String TYPE = ("5");
    private static final String USERDATA = ("6");
    private static final String RESID = ("7");
    private static final String CALLBACKURL = ("8");
    private static final String AREAID = ("9");
    private static final String LINEGATEWAYID = ("A");
    private static final String CLOSED = ("B");

    /**每个业务自己的字段的前缀**/
    private static final String inner_hash_field_prefix = "__";

    private String getInnerField(String field){
        return inner_hash_field_prefix + field;
    }

    private String getHashField(String key){
        return key.replace(inner_hash_field_prefix,"");
    }

    private boolean isInnerField(String key){
        return key.startsWith(inner_hash_field_prefix);
    }

    @Override
    public void save(BusinessState state) {
        if(state == null){
            return;
        }
        if(state.getId() == null){
            return;
        }
        try{
            String key = getKey(state.getId());
            Map<String,String> datas = new MapBuilder<String,String>()
                    .putIfNotEmpty(TENANTID,state.getTenantId())
                    .putIfNotEmpty(APPID,state.getAppId())
                    .putIfNotEmpty(SUBACCOUNTID,state.getSubaccountId())
                    .putIfNotEmpty(ID,state.getId())
                    .putIfNotEmpty(TYPE,state.getType())
                    .putIfNotEmpty(USERDATA,state.getUserdata())
                    .putIfNotEmpty(RESID,state.getResId())
                    .putIfNotEmpty(CALLBACKURL,state.getCallBackUrl())
                    .putIfNotEmpty(AREAID,state.getAreaId())
                    .putIfNotEmpty(LINEGATEWAYID,state.getLineGatewayId())
                    .putIfNotEmpty(CLOSED,state.getClosed()==null?null:state.getClosed().toString())
                    .build();
            if(state.getBusinessData()!=null){
                Set<Map.Entry<String,String>> entrySet = state.getBusinessData().entrySet();
                for (Map.Entry<String,String> entry : entrySet){
                    datas.put(getInnerField(entry.getKey()),entry.getValue());
                }
            }
            redisCacheService.hputAll(key, datas);
            redisCacheService.expire(key,EXPIRE_START);
        }catch (Throwable t){
            logger.error(String.format("保存state失败,state=%s",state),t);
        }
    }

    @Override
    public BusinessState get(String id) {
        BusinessState state = null;
        try{
            Map datas = redisCacheService.hgetAll(getKey(id));
            if(datas!=null && datas.size()>0){
                BusinessState.Builder builder = new BusinessState.Builder();
                Set<Map.Entry<String,String>> entrySet = datas.entrySet();
                Map<String,String> bussinessDate = new HashMap<>();
                for (Map.Entry<String,String> entry : entrySet){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if(value == null)
                        continue;
                    if(isInnerField(key)){
                        bussinessDate.put(getHashField(key),value);
                        continue;
                    }
                    switch (key){
                        case TENANTID:
                            builder.setTenantId(value);
                            break;
                        case APPID:
                            builder.setAppId(value);
                            break;
                        case SUBACCOUNTID:
                            builder.setSubaccountId(value);
                            break;
                        case ID:
                            builder.setId(value);
                            break;
                        case TYPE:
                            builder.setType(value);
                            break;
                        case USERDATA:
                            builder.setUserdata(value);
                            break;
                        case RESID:
                            builder.setResId(value);
                            break;
                        case CALLBACKURL:
                            builder.setCallBackUrl(value);
                            break;
                        case AREAID:
                            builder.setAreaId(value);
                            break;
                        case LINEGATEWAYID:
                            builder.setLineGatewayId(value);
                            break;
                        case CLOSED:
                            builder.setClosed(Boolean.parseBoolean(value));
                            break;
                    }
                }
                builder.setBusinessData(bussinessDate);
                state = builder.build();
            }
        }catch (Throwable t){
            logger.error(String.format("获取state失败,id=%s",id),t);
        }
        return state;
    }

    public boolean closed(String id){
        boolean result = Boolean.parseBoolean((String)redisCacheService.hget(getKey(id),CLOSED));
        if(logger.isDebugEnabled()){
            logger.debug("businessStateid={},closed={}",id,result);
        }
        return result;
    }

    public String subaccountId(String id){
        String result = (String)redisCacheService.hget(getKey(id),SUBACCOUNTID);
        if(logger.isDebugEnabled()){
            logger.debug("businessStateid={},subaccountId={}",id,result);
        }
        return result;
    }

    private void updateField(String id,String field,String value){
        String key = getKey(id);
        try{
            redisCacheService.hput(key,field,value);
            redisCacheService.expire(key,EXPIRE_START);
        }catch (Throwable t){
            logger.error(String.format("update business state 失败,id=%s,field=%s,value=%s",id,field,value),t);
        }
    }

    @Override
    public void updateUserdata(String id,String userdata) {
        updateField(id,USERDATA,userdata);
    }

    @Override
    public void updateResId(String id,String resId) {
        updateField(id,RESID,resId);
    }

    @Override
    public void updateCallBackUrl(String id,String callBackUrl) {
        updateField(id,CALLBACKURL,callBackUrl);
    }

    @Override
    public void updateAreaId(String id,String areaId) {
        updateField(id,AREAID,areaId);
    }

    @Override
    public void updateLineGatewayId(String id,String lineGatewayId) {
        updateField(id,LINEGATEWAYID,lineGatewayId);
    }

    @Override
    public void updateClosed(String id,Boolean closed) {
        updateField(id,CLOSED,closed==null?Boolean.FALSE.toString():closed.toString());
    }

    @Override
    public void updateInnerField(String id,String field,String value){
        updateField(id,getInnerField(field),value);
    }

    /****
     * 批量修改 state里的map的属性
     * @param id
     * @param params key1,value1,key2,value2的 数组
     */
    @Override
    public void updateInnerField(String id,String... params){
        if(params == null || params.length == 0 || params.length%2 != 0){//键值不匹配
            throw new IllegalArgumentException();
        }
        String key = getKey(id);
        try{
            Map<String,String> map = new HashMap<>();
            for (int i = 0,len = params.length; i < len; i=i+2) {
                map.put(getInnerField(params[i]),params[i+1]);
            }
            if(map.size()>0){
                redisCacheService.hputAll(key,map);
                redisCacheService.expire(key,EXPIRE_START);
            }
        }catch (Throwable t){
            logger.error(String.format("update business state 失败,id=%s,params=%s",id,params),t);
        }
    }

    @Override
    public void updateInnerField(String id,List<String> params){
        if(params ==null || params.size()==0){
            throw new IllegalArgumentException();
        }
        this.updateInnerField(id,params.toArray(new String[params.size()]));
    }
    @Override
    public void deleteInnerField(String id,String... fields){
        String key = getKey(id);
        try{
            String[] fs = new String[fields.length];
            for (int i = 0,len=fields.length; i < len; i++) {
                fs[i] = getInnerField(fields[i]);
            }
            redisCacheService.hdel(key,fs);
            redisCacheService.expire(key,EXPIRE_START);
        }catch (Throwable t){
            logger.error(String.format("delete business state field失败,id=%s,fields=%s",id,fields),t);
        }
    }

    @Override
    public void delete(String id) {
        try{
            if(redisCacheService.exists(getKey(id))){
                this.updateClosed(id,Boolean.TRUE);
                redisCacheService.expire(getKey(id),EXPIRE_RELEASE);
            }
        }catch (Throwable t){
            logger.error(String.format("删除state失败,id=%s",id),t);
        }
    }
}
