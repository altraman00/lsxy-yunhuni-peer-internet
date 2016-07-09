package com.lsxy.framework.core.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;


/**
 * 使用jackson json库序列化对象
 * @author tandy
 *
 */
public class JSONUtil2 {
	/**
	 * 将一个对象json化
	 * 
	 * @param obj
	 * @return
	 */
	public static String objectToJson(Object obj) {
		return objectToJson(obj,false);
	}
	/**
	 * 对象格式化，支持格式化输出
	 * @param obj
	 * @param pretty
	 * @return
	 */
	public static String objectToJson(Object obj,boolean pretty) {
		ObjectMapper om = new ObjectMapper();
		String result = null;
		try {
			SimpleFilterProvider fProvider = new SimpleFilterProvider();
			fProvider.setFailOnUnknownId(false);
			om.setFilters(fProvider);
			if(pretty) {
				result = om.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			}else{
				result = om.writeValueAsString(obj);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 串行化指定对象的指定字段
	 * 指定对象必须实现JSONUtil2Serialization接口
	 * @param o
	 * @param fields
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String objectToJsonInclude(Object o, String... fields)
			 {
		if (fields.length <= 0)
			return null;
		SimpleFilterProvider fProvider = new SimpleFilterProvider();
		//object must implements JSONUtil2Serialization interface
		fProvider.addFilter("fieldFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));
		ObjectMapper mapper = new ObjectMapper();
		mapper.setFilters(fProvider);
		String json = null;
		try {
			json = mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 对象串行化，但是排除序列化fields参数中指定的字段
	 *  指定对象必须实现JSONUtil2Serialization接口
	 * @param o
	 * @param fields
	 * @return
	 */
	public static String objectToJsonExclude(Object o, String... fields)
			 {
		ObjectMapper mapper = new ObjectMapper();
		// Exclude Null Fields
		mapper.setSerializationInclusion(Include.NON_NULL);
		FilterProvider fProvider = new SimpleFilterProvider().addFilter(
				"fieldFilter",
				SimpleBeanPropertyFilter.serializeAllExcept(fields));
		mapper.setFilters(fProvider);
		String json = null;
		try {
			json = mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 从json串构建一个指定类型的类对象实例
	 * @param json
	 * @param valueType
	 * @return
	 */
	public static <T> T fromJson(String json,Class<T> valueType){
		ObjectMapper om = new ObjectMapper();
		T obj = null;
		try {
			//针对json不认识的字段忽略掉，不要报错
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
			obj = om.readValue(json, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * Map转JSON
	 * @param map
	 * @return
	 */
	public static String map2json(Map<String, Object> map) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		
		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	/**
	 * 获取指定json串中的某一个属性的json子串
	 * if whole json is {a:1,person:{id:'111'}}
	 * but just need person sub json string  {id:'111'}  , u can using the method:
	 * getFieldJsonFromJson(json,'person') 
	 * fieldName支持子串的子串  比如session.person可以获取到{a:1,session:{id:'',person:{id:''}}
	 * @param json
	 * @param fieldName
	 * @return
	 * 		sub json
	 */
	public static String getFieldJsonFromJson(String json,String fieldName){
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		try {
			JsonNode actualObj = mapper.readValue(json,JsonNode.class);
			JsonNode jn = null;
			if(fieldName.indexOf(".") >= 0){
				String fieldNames[] = fieldName.split("\\.");
				JsonNode iteratorJsonNode = actualObj;
				for(int i=0;i<fieldNames.length;i++){
					iteratorJsonNode = iteratorJsonNode.get(fieldNames[i]);
					if(iteratorJsonNode == null)
						break;
				}
				jn = iteratorJsonNode;
			}else{
				jn = actualObj.get(fieldName);
			}
			if(jn != null){
				result = jn.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取指定json的某一个属性节点对象
	 * @param json
	 * @param fieldName
	 * @return
	 */
	public static JsonNode getFieldJsonNodeFromJson(String json,String fieldName){
		ObjectMapper mapper = new ObjectMapper();
		JsonNode result = null;
		try {
			JsonNode actualObj = mapper.readValue(json,JsonNode.class);
			JsonNode jn = null;
			if(fieldName.indexOf(".") >= 0){
				String fieldNames[] = fieldName.split("\\.");
				JsonNode iteratorJsonNode = actualObj;
				for(int i=0;i<fieldNames.length;i++){
					iteratorJsonNode = iteratorJsonNode.get(fieldNames[i]);
					if(iteratorJsonNode == null)
						break;
				}
				jn = iteratorJsonNode;
			}else{
				jn = actualObj.get(fieldName);
			}
			if(jn != null){
				result = jn;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		String json = "{\"session\":{\"sessionStatus\":2,\"person\":{\"id\":\"008@hesong\",\"displayName\":\"使者\"},\"isMulit\":false,\"endTime\":{\"time\":1413771600000},\"isEndByCustomer\":false,\"isEndByUser\":true,\"startTime\":{\"time\":1413771000000},\"id\":\"1\",\"sessionID\":\"1\",\"zxappid\":\"\",\"mulitUserIds\":\"\",\"cust\":{\"tenantUn\":\"hesong\",\"id\":\"1\",\"source\":\"\",\"displayName\":\"使者\"}},\"queueId\":123}";
		JsonNode jn = JSONUtil2.getFieldJsonNodeFromJson(json, "session.person.id");
		System.out.println(jn.asText());
	}
}
