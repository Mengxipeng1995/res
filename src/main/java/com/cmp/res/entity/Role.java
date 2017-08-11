package com.cmp.res.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cmp_role")
public class Role extends ID{
	
	/**
	 * 角色名
	 */
	private  String roleName;
	
	/**
	 * 角色等级  
	 * 角色等级数值越小，权限越高。
	 * 管理员只能操作角色等级数值大于自己的用户。
	 * 角色一经创建则不允许修改角色等级
	 * 取值范围：1-999999
	 */
	private Integer roleLevel;
	
	/**
	 * 角色描述
	 */
	private String  roleDesc;
	
	
	/**
	 * 该角色被那些用户拥有
	 */
	private Set<User> users;
	
	/**
	 * 角色所拥有的权限
	 */
	private Set<Menu> menus;
	
	private Set<Resource> resources;
	
	 @ManyToMany(mappedBy="roles")
	 @JsonIgnore
		public Set<User> getUsers() {
			return users;
		}

		/**
		 * @param users the users to set
		 */
		public void setUsers(Set<User> users) {
			this.users = users;
		}
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * @return the roleDesc
	 */
	public String getRoleDesc() {
		return roleDesc;
	}
	/**
	 * @param roleDesc the roleDesc to set
	 */
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT) 
	 public Set<Menu> getMenus() {
			return menus;
		}

		/**
		 * @param menus the menus to set
		 */
		public void setMenus(Set<Menu> menus) {
			this.menus = menus;
		}

		public Integer getRoleLevel() {
			return roleLevel;
		}

		public void setRoleLevel(Integer roleLevel) {
			this.roleLevel = roleLevel;
		}
		@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
		@Fetch(FetchMode.SUBSELECT) 
		public Set<Resource> getResources() {
			return resources;
		}

		public void setResources(Set<Resource> resources) {
			this.resources = resources;
		}
	
	
		

}
