package com.lsxy.framework.rpc.api;

import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.regex.Pattern;

import static java.lang.System.currentTimeMillis;

/**
 * Created by tandy on 16/8/2.
 * 用于传输的消息体
 *
 */
public abstract class RPCMessage implements Serializable {


    protected static final String EQ = "=";
    protected static final String AND = "&";
    protected static final String SPACE = " ";
    protected static final String EQ_ENCODE = "%3D";
    protected static final String AND_ENCODE = "%26";
    protected static final String SPACE_ENCODE = "%20";

    protected final static Pattern eq_pattern = Pattern.compile(EQ);
    protected final static Pattern and_pattern = Pattern.compile(AND);
    protected final static Pattern space_pattern = Pattern.compile(SPACE);
    protected final static Pattern eqencode_pattern = Pattern.compile(EQ_ENCODE);
    protected final static Pattern andencode_pattern = Pattern.compile(AND_ENCODE);
    protected final static Pattern spaceencode_pattern = Pattern.compile(SPACE_ENCODE);


    private static final Logger logger = LoggerFactory.getLogger(RPCMessage.class);
    // 会话标识 请求对象和响应对象是匹配的
    private String sessionid;

    //请求对象的时间戳,如果当前对象为响应对象,该时间戳应该为对应请求对象的时间戳
    private long timestamp = 0;

    //消息体,可以传输任何可序列化为二进制的数据
    private String body;		//BC

    //尝试发送次数  消息发送失败后,会尝试重新发送
    protected int tryTimes=0;

    //最后一次尝试发送的时间戳
    protected long lastTryTimestamp = 0L;


    /**
     * 标记一下尝试发送相关标记,用以审计
     */
    public void tryWriteMark(){
        tryTimes ++;
        lastTryTimestamp = currentTimeMillis();
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getBody() {
        return decode(body);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

//    /**
//     * body 支持任何对象类型 非字符串类型的对象会被序列化成对象数组进行传递
//     * @param obj
//     */
//    public void setBody(Object obj){
//        this.setBody(serializeObject(obj));
//    }

    /**
     * 获取字符串形式的body
     * @return
     */
    public String getBodyAsString(){
//        String sBody = "";
//        if(this.getBody() != null && this.getBody().length > 0) {
//            Object bodyObjet = this.getBodyAsObject();
//            if(bodyObjet != null)
//                sBody = bodyObjet.toString();
//        }
//        return sBody;
        return this.body;
    }


//    /**
//     * 获取body作为一个object对象
//     * @return
//     */
//    public Object getBodyAsObject(){
//        ByteArrayInputStream bais = new ByteArrayInputStream(this.getBody());
//        ObjectInputStream ois = null;
//        Object obj = null;
//        try {
//            ois = new ObjectInputStream(bais);
//            obj = ois.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            logger.error("IO异常",e);
//        }finally{
//            if(ois != null){
//                try {
//                    ois.close();
//                } catch (IOException e) {
//                    logger.error("IO异常",e);
//                }
//            }
//        }
//        return obj;
//    }

    /**
     * 序列化一个对象成为一个字节数组
     * @param obj
     * @return
     */
    private byte[] serializeObject(Object obj){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] result = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            result = bos.toByteArray();
        } catch (IOException e) {
            logger.error("IO异常",e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
            }
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }
        return result;
    }


    /**
     * 序列化方法
     * @return
     */
    public String getSerializeString(){
        String result = this.serialize();
        return result;
    }
    /**
     * 串行化抽象方法
     * @return
     */
    public abstract String serialize();


    public static RPCMessage unserialize(String msg){
        RPCMessage rpcMessage = null;
        if(StringUtil.isNotEmpty(msg)){
            if(msg.startsWith("RQ")){
                rpcMessage = RPCRequest.unserialize(msg);
            }else if (msg.startsWith("RP")){
                rpcMessage = RPCResponse.unserialize(msg);
            }
        }
        return rpcMessage;
    }

//    public static RPCMessage buildFromBase64(String base64){
//        if(logger.isDebugEnabled()){
//            logger.debug("序列化RPC MESSAGE:"+base64);
//        }
//        long startdt = System.currentTimeMillis();
//        byte[] bytes = Base64.decodeBase64(base64);
//        RPCMessage message = null;
//        try {
//            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
//            message = (RPCMessage) objectInputStream.readObject();
//            if(logger.isDebugEnabled()){
//                logger.debug("反序列化RPC对象耗时：{}ms",System.currentTimeMillis() - startdt);
//            }
//
//            return message;
//        } catch (Exception e) {
//            logger.error("RPC序列化异常:"+base64,e);
//        }
//        return message;
//    }


    /**
     * 将=转化为%3D
     * &转为%26
     * @return
     */
    public static String encode(String value){
        if(value == null){
            return null;
        }
        if(value.indexOf(EQ) > -1){
            value = eq_pattern.matcher(value).replaceAll(EQ_ENCODE);
        }
        if(value.indexOf(AND) > -1){
            value = and_pattern.matcher(value).replaceAll(AND_ENCODE);
        }
        if(value.indexOf(SPACE) > -1){
            value = space_pattern.matcher(value).replaceAll(SPACE_ENCODE);
        }
        return value;
    }

    public static String decode(String value){
        if(value == null){
            return null;
        }
        if(value.indexOf(EQ_ENCODE) > -1){
            value = eqencode_pattern.matcher(value).replaceAll(EQ);
        }
        if(value.indexOf(AND_ENCODE) > -1){
            value = andencode_pattern.matcher(value).replaceAll(AND);
        }
        if(value.indexOf(SPACE_ENCODE) > -1){
            value = spaceencode_pattern.matcher(value).replaceAll(SPACE);
        }
        return value;
    }
}
