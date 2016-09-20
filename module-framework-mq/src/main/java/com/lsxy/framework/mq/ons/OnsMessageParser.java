package com.lsxy.framework.mq.ons;

import com.lsxy.framework.mq.api.MQEvent;
import com.lsxy.framework.mq.api.MQMessageParser;
import com.lsxy.framework.mq.exceptions.InvalidMQEventMessageException;
import net.sf.json.JSONObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.lsxy.framework.cache.FrameworkCacheConfig.logger;

/**
 * Created by Tandy on 2016/7/23.
 */
@Component
@ConditionalOnProperty(value = "global.mq.provider", havingValue = "ons", matchIfMissing = false)
public class OnsMessageParser implements MQMessageParser {
    @Override
    public MQEvent parse(String message) throws InvalidMQEventMessageException {

		JSONObject jsonObject = null;
		try{
			jsonObject = JSONObject.fromObject(message);
		}catch(Exception ex){
			throw new InvalidMQEventMessageException(ex);
		}
		Assert.notNull(jsonObject,"无效的json 消息字符串");
		String eventClassName = jsonObject.getString("eventName");
		Assert.notNull(eventClassName,"无效的事件消息体："+message);
		MQEvent result = null;
		try {
			logger.debug("实例化事件对象："+eventClassName);
			Class clazz = Class.forName(eventClassName);
			Object obj = JSONObject.toBean(jsonObject, clazz);
			logger.debug("事件对象赋值："+jsonObject);

			if(obj instanceof MQEvent){
				result = (MQEvent) obj;
				logger.debug("解析出事件类:"+result.getEventName());
			}
		} catch (ClassNotFoundException | SecurityException e) {
			throw new InvalidMQEventMessageException(e);
		}
		return result;

    }
}
