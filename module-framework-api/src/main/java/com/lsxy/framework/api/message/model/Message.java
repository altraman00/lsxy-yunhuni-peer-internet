package com.lsxy.framework.api.message.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 消息
 * Created by zhangxb on 2016/7/4.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_message")
public class Message extends IdEntity{
    private int type;//消息类型
    private String content;//消息内容
    private Date createTime;//创建时间
    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}