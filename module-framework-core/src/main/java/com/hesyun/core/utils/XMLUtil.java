package com.hesyun.core.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;


public class XMLUtil {

	public static String objToXml(Object obj) {
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj);
	}
	
	public static Object xmlToObj(String message) {
		return xstream.fromXML(message);
		
	}

    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXML(String message)
            throws IOException, DocumentException {
        Map<String, String> messageMap = new HashMap<String, String>();
        
        Document doc = DocumentHelper.parseText(message);
        Element root = doc.getRootElement();
        for (Element e : (List<Element>) root.elements()) {
            messageMap.put(e.getName(), e.getText());
        }
        return messageMap;
    }
    
    @SuppressWarnings("unchecked")
    public static JSONObject parseXMLToJson(String message)
            throws IOException, DocumentException {
        JSONObject json = new JSONObject();
        
        Document doc = DocumentHelper.parseText(message);
        Element root = doc.getRootElement();
        for (Element e : (List<Element>) root.elements()) {
            json.put(e.getName(), e.getText());
        }
        return json;
    }
    
	public static void main(String[] args) throws IOException, DocumentException {
		String xx ="<xml>"+
				"<ToUserName><![CDATA[toUser]]></ToUserName>"+
				"<FromUserName><![CDATA[fromUser]]></FromUserName>"+
				"<CreateTime>1357290913</CreateTime>"+
				"<MsgType><![CDATA[voice]]></MsgType>"+
				"<MediaId><![CDATA[media_id]]></MediaId>"+
				"<Format><![CDATA[Format]]></Format>"+
				"<MsgId>1234567890123456</MsgId>"+
				"</xml>";
		Map<String,String> obj = parseXML(xx);
		System.out.println(obj.get("ToUserName"));
	}
	
	/**
	 * 扩展xstream，使其支持CDATA块
	 *
	 */
	private static XStream xstream = new XStream(new XppDriver() {
	    public HierarchicalStreamWriter createWriter(Writer out) {
	        return new PrettyPrintWriter(out) {
	            // 对所有xml节点的转换都增加CDATA标记 
	            boolean cdata = true;

	            @SuppressWarnings({ "rawtypes" })
	            public void startNode(String name, Class clazz) {
	                super.startNode(name, clazz);
	            }

	            protected void writeText(QuickWriter writer, String text) {
	                if (cdata) {
	                    writer.write("<![CDATA[");
	                    writer.write(text);
	                    writer.write("]]>");
	                } else {
	                    writer.write(text);
	                }
	            }
	        };
	    }
	});
}
