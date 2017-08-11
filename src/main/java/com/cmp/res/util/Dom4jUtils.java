package com.cmp.res.util;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class Dom4jUtils {

	public static Document getDocument(File xml) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(xml);
		return document;
	}

	public static String getValueByAttributeIsNull(List<Element> elements, String attrName) {
		for (Element e : elements) {
			if (StringUtils.isBlank(e.attributeValue(attrName))) {
				return e.getText();
			}
		}
		return null;
	}

	public static String getValueByAttributeNotNull(List<Element> elements, String attrName) {
		for (Element e : elements) {
			if (StringUtils.isNotBlank(e.attributeValue(attrName))) {
				return e.getText();
			}
		}
		return null;
	}

	public static String getValueByAttribute(List<Element> elements, String attrName, String attrValue) {
		for (Element e : elements) {
			if (StringUtils.isBlank(e.attributeValue(attrName))) {
				continue;
			}
			if (e.attributeValue(attrName).equalsIgnoreCase(attrValue)) {
				return e.getText();
			}
		}
		return null;
	}

	public static String getValuesByElements(List<Element> elements, String separator) {
		StringBuilder sb = new StringBuilder();
		for (Element e : elements) {
			sb.append(e.getText());
			if (separator != null) {
				sb.append(separator);
			}
		}
		if (sb.length() <= 0) {
			return null;
		}
		return sb.toString();
	}

	public static Element getElementByAttribute(List<Element> elements, String attrName, String attrValue) {
		for (Element e : elements) {
			if (StringUtils.isBlank(e.attributeValue(attrName))) {
				continue;
			}
			if (e.attributeValue(attrName).equalsIgnoreCase(attrValue)) {
				return e;
			}
		}
		return null;
	}


	public static void main(String[] args) throws DocumentException {

		 Document document=getDocument(new File("d:\\main.xml"));
		 Element item=document.getRootElement();
		
		 StringBuilder sb=new StringBuilder();
		 //System.out.println(item.getNamespace());
		 //item.remove(item.getNamespace());
		 //System.out.println(item.getNamespace());
		/* List<Element> elements=item.elements();
		 for(Element e:elements){
			 if(e.getName().equalsIgnoreCase("title")||e.getName().equalsIgnoreCase("info")){
			 continue;
			 }
			 sb.append(e.asXML());
		 }*/
		 System.out.println(item.element("part").attributeValue("id"));
		 List<Element> elements=item.element("part").elements("chapter");
		 Element chapter=elements.get(1);
		 Element title=chapter.element("title");
		 System.out.println(title.asXML());
		 List<Element> list=title.elements();
		 System.out.println(title.getStringValue());
		 System.out.println(((Element)chapter.element("title").elements().get(0)).asXML());
		/* System.out.println(chapter.element("titleabbrev").asXML());
		 System.out.println(((Element)chapter.element("titleabbrev").elements().get(0)).attributeValue("type"));*/

		
	}

}
