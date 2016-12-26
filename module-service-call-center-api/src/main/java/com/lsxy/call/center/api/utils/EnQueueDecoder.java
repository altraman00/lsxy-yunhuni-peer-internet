package com.lsxy.call.center.api.utils;

import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.framework.core.utils.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuwensheng on 2016/10/30.
 */
public class EnQueueDecoder {

    private static final Logger logger = LoggerFactory.getLogger(EnQueueDecoder.class);
    private final static XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

    static {
        xStream.processAnnotations(EnQueue.class);
        xStream.autodetectAnnotations(true);
    }

    public static EnQueue decode(String xml){
        if(StringUtil.isBlank(xml)){
            return null;
        }
        Long start = null;
        if(logger.isDebugEnabled()){
            start = System.currentTimeMillis();
        }
        EnQueue enQueue = null;
        try{
            enQueue = (EnQueue)xStream.fromXML(xml);
        }catch (Throwable t){
            logger.error("decode enqueue error",t);
        }
        if(logger.isDebugEnabled()){
            logger.debug("decode 耗时{}",(System.currentTimeMillis()-start));
        }
        return enQueue;
    }
}
