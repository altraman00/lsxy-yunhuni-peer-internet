package com.lsxy.framework.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tandy on 2016/6/22.
 * YUNHUNI REST API 请求对象
 * 支持POST GET PUT DELETE
 * <p>
 * 使用方法：
 * String url = restPrefixUrl + "/login";
 * Map<String,Object> formParams = new HashMap<>();
 * formParams.put("username","tanchang");
 * formParams.put("password","123");
 * <p>
 * RestResponse<UserRestToken> response = RestRequest.newInstance().post(url,formParams,UserRestToken.class);
 */
public class RestRequest {
    //请求标识
    private String id;

    private static final Logger logger = LoggerFactory.getLogger(RestRequest.class);
    //单实例请求对象
    private static RestRequest request;

    //安全API调用的请求对象
    private static RestRequest securityRequest;

    private RestTemplate restTemplate;

    //用于访问基于安全授权的api时需要使用security header的 token
    private String securityToken;

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    protected RestRequest(RestRequestConnectionConfig config) {
        this.id = UUIDGenerator.uuid();
        this.restTemplate = new RestTemplate(config.getHttpFactory());
        // 添加转换器
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());
//        messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        this.restTemplate.setMessageConverters(messageConverters);
    }

    /**
     * rest api get request method
     *
     * @param url              get 请求的目标url 地址
     * @param responseDataType 请求返回对象restresponse中data属性的数据类型
     * @param <T>              用户指定rest response返回对象中data属性的数据对象类
     * @param uriparams        url自动匹配替换的参数，如url为api/{a}/{b},参数为["1","2"],则解析的url为api/1/2，使用Map参数时，遵循按key匹配
     * @return
     * http://xxxx/{1}/{2}{3}{4}/?a={5}&c={6}&d={7} 注：{}里面一定要带一个字符，不然值设不进去
     */
    public <T> RestResponse<T> get(String url,Class<T> responseDataType, Object... uriparams) {
        return exchange(url,HttpMethod.GET,null,responseDataType,uriparams);
    }

    /**
     * 获取列表数据，可通过泛型指定列表数据内部对象类型
     * @param url
     * @param responseDataType
     * @param uriparams
     * @param <T>
     * @return
     */
    public <T> RestResponse<List<T>> getList(String url, Class<T> responseDataType, Object... uriparams) {
        RestResponse<List> resultResponse = get(url, List.class, uriparams);
        List<Object> list = resultResponse.getData();

        //转换为具体类型的对象列表
        List<T> concreteList = convertListToConcretList(list,responseDataType);
        RestResponse<List<T>> result = resultResponse.wrapIndicateTypeRestResponse(concreteList);
        return result;
    }

    /**
     * 获取翻页数据,可通过泛型指定翻页数据列表对象中的对象类型
     * @param url       请求URL
     * @param responseDataType  制定响应数据类型
     * @param uriparams                 可指定参数,改参数将会作为url通配符的替换值 {}{}
     * @param <T>
     * @return
     *          已分页对象返回相应指定对象的分页数据
     */
    public <T> RestResponse<Page<T>> getPage(String url, Class<T> responseDataType, Object... uriparams) {
        RestResponse<Page> restResponse = get(url, Page.class, uriparams);
        Page<Object> page = restResponse.getData();
        if(page == null){
            return restResponse.wrapIndicateTypeRestResponse(null);
        }

        //将对象列表转换为具体对象类型的列表
        List<T> concreteList = convertListToConcretList(page.getResult(),responseDataType);

        Page<T> pageResult = new Page<T>(page.getStartIndex(),page.getTotalCount(),page.getPageSize(),concreteList);
        RestResponse<Page<T>> resultResponse = restResponse.wrapIndicateTypeRestResponse(pageResult);
        return resultResponse;
    }


    /**
     * 转换列表对象List<Map> to List<T>
     * @param objList
     * @param concretDataType
     * @param <T>
     * @return
     */
    private <T> List<T> convertListToConcretList(List<?> objList,Class<T> concretDataType) {
        List<T> concreteList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        if(objList != null){
            objList.stream().filter(obj -> obj instanceof Map).forEach(obj -> {
                T mapperObject = mapper.convertValue(obj, concretDataType);
                if (mapperObject != null) {
                    concreteList.add(mapperObject);
                }
            });
        }
        return concreteList;
    }

    /**
     * rest api post request method
     *
     * @param url              目标地址
     * @param params           请求post 参数
     * @param responseDataType 返回对象类型
     * @param <T>              返回对象类型
     * @return
     */
    public <T> RestResponse<T> post(String url, Map<String, Object> params, Class<T> responseDataType, String... uriparams) {
        return exchange(url,HttpMethod.POST,params,responseDataType,uriparams);
    }

    /**
     * rest api post request method
     *
     * @param url              目标地址
     * @param payload           请求post 参数
     * @param responseDataType 返回对象类型
     * @param <T>              返回对象类型
     * @return
     */
    public <T> RestResponse<T> post(String url, String payload, Class<T> responseDataType, String... uriparams) {
        return exchange(url,HttpMethod.POST,payload,responseDataType,uriparams);
    }

    /**
     * rest api post request method
     *
     * @param url              目标地址
     * @param payload           请求post 参数
     * @param responseDataType 返回对象类型
     * @param <T>              返回对象类型
     * @return
     */
    public <T> RestResponse<T> post(String url, String payload, Class<T> responseDataType,HttpHeaders headers, String... uriparams) {
        return exchange(url,HttpMethod.POST,payload,responseDataType,headers,uriparams);
    }
    /**
     * 公用的代码抽取
     * @param url              请求的目标url 地址
     * @param httpMethod       请求的方式
     * @param payload           请求参数  可以是Map或者是String 用于处理post body
     * @param responseDataType 请求返回对象restresponse中data属性的数据类型
     * @param <T>              用户指定rest response返回对象中data属性的数据对象类
     * @return
     */
    public <T> RestResponse<T> exchange(String url,HttpMethod httpMethod, Object payload, Class<T> responseDataType,Object... uriparams){
        return exchange(url,httpMethod,payload,responseDataType,null,uriparams);
    }

    /**
     * 公用的代码抽取
     * @param url              请求的目标url 地址
     * @param httpMethod       请求的方式
     * @param payload           请求参数  可以是Map或者是String 用于处理post body
     * @param responseDataType 请求返回对象restresponse中data属性的数据类型
     * @param <T>              用户指定rest response返回对象中data属性的数据对象类
     * @return
     */
    public <T> RestResponse<T> exchange(String url,HttpMethod httpMethod, Object payload, Class<T> responseDataType,HttpHeaders headers,Object... uriparams){
        RestResponse<T> restResponse = null;
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        HttpEntity entity = null;
        //构建请求头信息
        headers = buildHttpHeaders(url,httpMethod,payload,headers,uriparams);

        if(payload instanceof  Map){
            Map<String,Object> params = (Map<String, Object>) payload;
            if(params != null){
                params.keySet().stream().forEach(key -> requestEntity.add(key, MapUtils.getString(params, key, "")));
            }
            entity = new HttpEntity(requestEntity,headers);
        }else{
            entity = new HttpEntity(payload,headers);
        }

        if(logger.isDebugEnabled()){
            logger.debug("[{}]REST请求：{} ",this.id,url);
            logger.debug("[{}]REST请求参数：{}",this.id,payload);
            logger.debug("[{}]REST请求头：{}",this.id,entity.getHeaders());
        }
        long starttime = System.currentTimeMillis();
        HttpEntity<RestResponse> response = this.restTemplate.exchange(url, httpMethod, entity, RestResponse.class,uriparams);
        if (response != null) {
            restResponse = response.getBody();
            if (restResponse.isSuccess() && restResponse.getData() != null && responseDataType != null) {
                ObjectMapper mapper = new ObjectMapper();
                T obj = mapper.convertValue(restResponse.getData(), responseDataType);
                restResponse.setData(obj);
            }
        }
        if(logger.isDebugEnabled()){
            logger.debug("[{}]REST响应{}  花费: {}ms",this.id,restResponse,System.currentTimeMillis() - starttime);
        }
        return restResponse;
    }
    /**
     * 构建http header头信息
     * @return
     * @param url
     * @param httpMethod
     * @param params
     * @param uriparams
     */
    protected HttpHeaders buildHttpHeaders(String url, HttpMethod httpMethod,Object  params,HttpHeaders headers, Object[] uriparams) {
        if(headers == null){
            headers = new HttpHeaders();
        }
        if (StringUtil.isNotEmpty(this.securityToken)) {
            headers.set(SystemConfig.getProperty("global.rest.api.security.header", "X-YUNHUNI-API-TOKEN"), this.securityToken);
        }
        return headers;
    }


    /**
     * 不用指定返回对象类型的post
     *
     * @param url
     * @param params
     * @param <T>
     * @return
     */
    public <T> RestResponse<T> post(String url, Map<String, Object> params) {
        return post(url, params, null);
    }

    /**
     * 构建一个单一实例的请求对象
     * 因为每次安全请求的token有可能不一样，建议每次构建请求对象重新生成，避免多线程不安全性问题
     *
     * @return
     */
    public static RestRequest buildSecurityRequest(String token) {

        RestRequest securityRequest = new RestRequest(RestRequestConnectionConfig.defaultConfig());
        securityRequest.setSecurityToken(token);

        return securityRequest;
    }

    /**
     * 构建默认请求对象，每次相同，所以采用单例，不存在线程安全问题
     * @return
     */
    public static RestRequest buildRequest() {
        if (request == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("build request using default config for request");
            }
            request = new RestRequest(RestRequestConnectionConfig.defaultConfig());
        }
        return request;
    }
}
