package com.cmp.res.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cmp_menu")
public class Menu extends ID{
	/**
	 * 资源唯一标识串
	 */
	private String uniqueIdentifierString;
	
	
	private String name;
	
	private List<Menu> sonsMenu;
	
	private List<Menu> children;
	
	/**
     * 是否为叶子节点
     */
    private boolean leaf;
    
    private Menu parentMenu;
    
    /**
	 * 父节点id
	 */
	private Long parentid;
    
    private String text;
    
    private Boolean checkFlag; 

	/**
	 * @return the uniqueIdentifierString
	 */
	public String getUniqueIdentifierString() {
		return uniqueIdentifierString;
	}

	/**
	 * @return the parentid
	 */
	public Long getParentid() {
		return parentid;
	}

	/**
	 * @param parentid the parentid to set
	 */
	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	/**
	 * @param uniqueIdentifierString the uniqueIdentifierString to set
	 */
	public void setUniqueIdentifierString(String uniqueIdentifierString) {
		this.uniqueIdentifierString = uniqueIdentifierString;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sonsMenu
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentMenu", fetch = FetchType.LAZY) 
	public List<Menu> getSonsMenu() {
		return sonsMenu;
	}

	/**
	 * @param sonsMenu the sonsMenu to set
	 */
	public void setSonsMenu(List<Menu> sonsMenu) {
		this.sonsMenu = sonsMenu;
	}

	/**
	 * @return the leaf
	 */
	@Transient
	public boolean isLeaf() {
		return this.sonsMenu.size()==0?true:false;
	}

	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * @return the parentMenu
	 */
	@ManyToOne
	 @JoinColumn(name = "parentid",insertable=false,updatable=false)
	 @NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public Menu getParentMenu() {
		return parentMenu;
	}

	/**
	 * @param parentMenu the parentMenu to set
	 */
	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	/**
	 * @return the text
	 */
	@Transient
	public String getText() {
		 return this.name;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the children
	 */
	@Transient
	public List<Menu> getChildren() {
		return this.getSonsMenu();
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	/**
	 * @return the checkFlag
	 */
	@Transient
	public Boolean getCheckFlag() {
		return checkFlag;
	}

	/**
	 * @param checkFlag the checkFlag to set
	 */
	public void setCheckFlag(Boolean checkFlag) {
		this.checkFlag = checkFlag;
	}
	
	
    

}
