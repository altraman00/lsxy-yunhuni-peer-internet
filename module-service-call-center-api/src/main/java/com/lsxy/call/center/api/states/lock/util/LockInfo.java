package com.lsxy.call.center.api.states.lock.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsxy.framework.core.utils.JSONUtil2;
import sun.management.VMManagement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * Created by liuws on 2017/2/22.
 */
public class LockInfo {

    @JsonProperty("a")
    private long expires;

    @JsonProperty("b")
    private String mac;

    @JsonProperty("c")
    private long jvmPid;

    @JsonProperty("d")
    private long threadId;

    @JsonProperty("e")
    private int count;

    private static final transient String LOCAL_MAC = PlatformUtils.MACAddress();

    private static final transient int CURRENT_PID = PlatformUtils.JVMPid();

    public LockInfo incCount() {
        if (count == Integer.MAX_VALUE) {
            throw new Error("Maximum lock count exceeded");
        }
        ++count;
        return this;
    }

    public LockInfo decCount() {
        --count;
        return this;
    }

    public boolean currentThread() {
        return LOCAL_MAC.equals(mac) && (""+CURRENT_PID).equals(""+jvmPid) && (""+threadId).equals(""+Thread.currentThread().getId());
    }

    public static LockInfo fromString(String lockInfo) {
        try {
            return JSONUtil2.fromJson(lockInfo, LockInfo.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static LockInfo newForCurrThread(long expires) {
        LockInfo lockInfo = new LockInfo();
        lockInfo.setThreadId(Thread.currentThread().getId());
        lockInfo.setCount(1);
        lockInfo.setExpires(expires);
        lockInfo.setJvmPid(CURRENT_PID);
        lockInfo.setMac(LOCAL_MAC);
        return lockInfo;
    }

    public static LockInfo newForCurrThread() {
        LockInfo lockInfo = new LockInfo();
        lockInfo.setThreadId(Thread.currentThread().getId());
        lockInfo.setCount(1);
        lockInfo.setExpires(System.currentTimeMillis() + 60000);
        lockInfo.setJvmPid(CURRENT_PID);
        lockInfo.setMac(LOCAL_MAC);
        return lockInfo;
    }

    public long getExpires() {
        return expires;
    }

    public LockInfo setExpires(long expires) {
        this.expires = expires;
        return this;
    }

    public String getMac() {
        return mac;
    }

    public LockInfo setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public long getJvmPid() {
        return jvmPid;
    }

    public LockInfo setJvmPid(long jvmPid) {
        this.jvmPid = jvmPid;
        return this;
    }

    public long getThreadId() {
        return threadId;
    }

    public LockInfo setThreadId(long threadId) {
        this.threadId = threadId;
        return this;
    }

    public int getCount() {
        return count;
    }

    public LockInfo setCount(int count) {
        this.count = count;
        return this;
    }

    public static String toString(LockInfo lockInfo) {
        return JSONUtil2.objectToJson(lockInfo);
    }

    @Override
    public String toString() {
        return toString(this);
    }

    public boolean isSame(Object obj) {
        if(obj == null)
            return false;

        if(obj instanceof LockInfo){
            LockInfo info = (LockInfo) obj;
            return info.getMac().equals(mac)
                    && info.getJvmPid() == jvmPid
                    && info.getThreadId() == threadId
                    && info.getExpires() == expires
                    && info.getCount() == count;
        }
        return false;
    }
}
