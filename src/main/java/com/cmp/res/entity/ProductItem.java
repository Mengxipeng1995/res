package com.cmp.res.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 产品条目关联表
 * @author trs
 *
 */
@Entity
@Table(name="cmp_product_item")
public class ProductItem extends ID{
	
	/**
	 * 产品id
	 */
	private Long productid;
	
	/**
	 * 条目id
	 */
	private Long itemid;
	
	
	/**
	 *条目分类id
	 */
	private Long catid;
	/**
	 * 条目类型
	 * 0、普通条目
	 * 1、图片
	 * 2、视频
	 */
	private Integer type;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	private String itemTitle;
	
	private String itemKeywords;

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public Long getCatid() {
		return catid;
	}

	public void setCatid(Long catid) {
		this.catid = catid;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getItemKeywords() {
		return itemKeywords;
	}

	public void setItemKeywords(String itemKeywords) {
		this.itemKeywords = itemKeywords;
	}
	
	

}
