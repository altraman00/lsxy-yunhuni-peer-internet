package com.lsxy.area.agent.cti;

import com.lsxy.app.area.cti.BusAddress;
import com.lsxy.app.area.cti.Commander;
import com.lsxy.app.area.cti.RpcResultListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by tandy on 16/12/22.
 */
public class CTINode{
    private static final Logger logger = LoggerFactory.getLogger(CTINode.class);
    private final static String CTI_NODE_STATUS_GREEN="Green";
    private final static String CTI_NODE_STATUS_YELLOW="Yellow";
    private final static String CTI_NODE_STATUS_RED="Red";

    private String id;
    private String ip;
    private String unitId;
    private String pid;
    private boolean ready;
    private int cinCount;   //呼入累计值
    private int coutCount;  //呼出累计值
    private int cinNumber;  //当前呼入数量
    private int coutNumber;  //当前呼出数数量
    private String status;
    private Commander ctiCommander;

    public Commander getCtiCommander() {
        return ctiCommander;
    }

    public void setCtiCommander(Commander ctiCommander) {
        this.ctiCommander = ctiCommander;
    }

    public CTINode(String snode, String sServer) {
        this.id = snode;
        this.ip = sServer;
        String snodeSplit[] = snode.split("\\.");
        this.unitId = snodeSplit[1];
        this.pid = snodeSplit[2];
        this.ready = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getCinCount() {
        return cinCount;
    }

    public void setCinCount(int cinCount) {
        this.cinCount = cinCount;
    }

    public int getCoutCount() {
        return coutCount;
    }

    public void setCoutCount(int coutCount) {
        this.coutCount = coutCount;
    }

    public int getCinNumber() {
        return cinNumber;
    }

    public void setCinNumber(int cinNumber) {
        this.cinNumber = cinNumber;
        updateStatus();
    }

    public int getCoutNumber() {
        return coutNumber;
    }

    public void setCoutNumber(int coutNumber) {
        this.coutNumber = coutNumber;
        updateStatus();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void updateStatus(){
        int load = this.cinNumber + coutNumber;
        if((load)<500){
            this.status = CTI_NODE_STATUS_GREEN;
        }else if(load >= 500 && load <=1000){
            this.status = CTI_NODE_STATUS_YELLOW;
        }else{
            this.status = CTI_NODE_STATUS_RED;
        }
    }

    /**
     * 创建资源
     * @param name
     * @param params
     * @param rpcResultListener
     * @throws IOException
     */
    public String createResource(String name, Map<String, Object> params, RpcResultListener rpcResultListener) throws IOException, CreateCTIResoucesException {
        if(this.ctiCommander != null){
            return ctiCommander.createResource(new BusAddress((byte)Integer.parseInt(this.getUnitId()),(byte)Integer.parseInt(this.getPid())),name,params,rpcResultListener);
        }else{
            logger.error("node is not ready , can't create resource for : " + name + "("+params+") and node info is "+ this);
            throw new CreateCTIResoucesException(name,params,this);
        }

    }

    @Override
    public String toString() {
        return String.format("cti node:%s(%s) => cint count : %s , cout count : %s , cin number : %s , cout number : %s",id,ip,cinCount,coutCount,cinNumber,coutNumber);
    }

    /**
     * 操作CTI资源
     * @param resId
     * @param method
     * @param params
     * @param o
     * @throws IOException
     */
    public void operateResource(String resId, String method, Map<String, Object> params, RpcResultListener o) throws IOException, OperateCTIResoucesException {
        if(this.ctiCommander != null){
            ctiCommander.operateResource(new BusAddress((byte)Integer.parseInt(this.getUnitId()),(byte)Integer.parseInt(this.getPid())),resId,method,params,o);
        }else{
            logger.error("node is not ready , can't operate resource for : " + resId +"=>"+ method+"("+params+") and node info is "+ this);
            throw new OperateCTIResoucesException(resId,method,params,this);
        }
    }

    public int getLoadValue() {
        return this.cinNumber + this.coutNumber;
    }
}
