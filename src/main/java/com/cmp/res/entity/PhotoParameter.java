package com.cmp.res.entity;

import net.sf.json.JSONObject;

public class PhotoParameter {
	//
	
	private Double x;
	
	private Double y;
	
	private Float height;
	
	private Float width;
	
	private Float rotate;






	public Double getX() {
		return x;
	}






	public void setX(Double x) {
		this.x = x;
	}






	public Double getY() {
		return y;
	}






	public void setY(Double y) {
		this.y = y;
	}






	public Float getHeight() {
		return height;
	}






	public void setHeight(Float height) {
		this.height = height;
	}






	public Float getWidth() {
		return width;
	}






	public void setWidth(Float width) {
		this.width = width;
	}






	public Float getRotate() {
		return rotate;
	}






	public void setRotate(Float rotate) {
		this.rotate = rotate;
	}






	public static void main(String[] args) {
		String json="{'x':25.300000000000004,'y':26.299999999999994,'height':202.4,'width':202.4,'rotate':90}";
		 JSONObject jsonobject = JSONObject.fromObject(json);
		 PhotoParameter pp=(PhotoParameter) JSONObject.toBean(jsonobject, PhotoParameter.class);
		 System.out.println(pp.getHeight());
//		Float f=new Float(123.1);
//		System.out.println(f.intValue());
	}
	
	

}
