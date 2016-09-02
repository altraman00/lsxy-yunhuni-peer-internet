package com.lsxy.area.server.util.ivr.act.domain;

import java.util.List;

/**
 * Created by liuws on 2016/9/2.
 */
public class Item {
    private String tag;
    private List<Attribute> attributes;
    private String text;
    private List<Item> items;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag.toLowerCase();
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}