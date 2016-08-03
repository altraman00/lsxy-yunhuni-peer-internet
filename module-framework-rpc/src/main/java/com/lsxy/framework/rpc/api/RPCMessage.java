package com.lsxy.framework.rpc.api;

import io.netty.util.CharsetUtil;

import java.io.*;

/**
 * Created by tandy on 16/8/2.
 * 用于传输的消息体
 *
 */
public class RPCMessage {

    // 会话标识 请求对象和响应对象是匹配的
    private String sessionid;

    //请求对象的时间戳,如果当前对象为响应对象,该时间戳应该为对应请求对象的时间戳
    private long timestamp = 0;

    //消息体,可以传输任何可序列化为二进制的数据
    private byte[] body;		//BC

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * body 支持任何对象类型 非字符串类型的对象会被序列化成对象数组进行传递
     * @param obj
     */
    public void setBody(Object obj){
        this.setBody(serializeObject(obj));
    }

    /**
     * 获取字符串形式的body
     * @return
     */
    public String getBodyAsString(){
        String sBody = "";
        if(this.getBody() != null && this.getBody().length > 0) {
            Object bodyObjet = this.getBodyAsObject();
            if(bodyObjet != null)
                sBody = bodyObjet.toString();
        }
        return sBody;
    }


    /**
     * 获取body作为一个object对象
     * @return
     */
    public Object getBodyAsObject(){
        ByteArrayInputStream bais = new ByteArrayInputStream(this.getBody());
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

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
            e.printStackTrace();
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


}
