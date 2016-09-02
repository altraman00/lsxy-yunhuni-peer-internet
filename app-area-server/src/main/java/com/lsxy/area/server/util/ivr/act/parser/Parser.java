package com.lsxy.area.server.util.ivr.act.parser;

import com.lsxy.area.server.util.ivr.act.domain.Action;
import com.lsxy.area.server.util.ivr.act.domain.Attribute;
import com.lsxy.area.server.util.ivr.act.domain.Item;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liuws on 2016/9/2.
 */
public class Parser {

    public static Action parse(String xml){
        Action action = new Action();
        Item rootItem = new Item();
        try{
            Document doc = DocumentHelper.parseText(xml);
            Element rootEle = doc.getRootElement();
            Element next = rootEle.element("next");
            action.setAction(rootEle.getName());
            action.setItem(rootItem);
            if(next != null){
                action.setNext(next.getTextTrim());
                rootEle.remove(next);
            }
            parseXMLItem(rootEle,rootItem);
        }catch (Throwable t){
            throw new IllegalArgumentException(t);
        }
        return action;
    }

    private static void parseXMLItem(Element ele,Item item){
        List<Attribute> atts = new ArrayList<Attribute>();
        item.setTag(ele.getName());
        item.setText(ele.getTextTrim());
        item.setAttributes(atts);
        for(int i=0,attributeCount=ele.attributeCount();i<attributeCount;i++){
            org.dom4j.Attribute attribute = ele.attribute(i);
            Attribute att = new Attribute();
            att.setKey(attribute.getName());
            att.setValue(attribute.getValue());
            atts.add(att);
        }
        Iterator iterator = ele.elementIterator();
        List<Item> subItems = new ArrayList<>();
        while(iterator.hasNext()){
            Element sele = (Element)iterator.next();
            Item sitem = new Item();
            subItems.add(sitem);
            parseXMLItem(sele,sitem);
        }
        item.setItems(subItems);
    }
}
