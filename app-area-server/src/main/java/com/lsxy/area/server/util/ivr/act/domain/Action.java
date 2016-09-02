package com.lsxy.area.server.util.ivr.act.domain;

import com.lsxy.framework.core.utils.JSONUtil2;

/**
 * Created by liuws on 2016/9/2.
 */
public class Action{
    private String action;
    private Item item;
    private String next;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action.toLowerCase();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString(){
        return JSONUtil2.objectToJson(this);
    }
}
