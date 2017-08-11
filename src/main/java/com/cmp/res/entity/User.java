package com.cmp.res.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity(name="cmp_user")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames="userName")})
public class User extends ID{
	
	/**
	 * 登录名
	 */
	private String userName;
	
	/**
	 * 用户昵称
	 */
	private String nickName;
	
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 用户等级    根据用户所拥有的角色最高等级决定
	 * 如：用户所拥有角色为：普通管理员（角色等级5）、普通用户（角色等级6），那么用户等级为5
	 */
	private Integer userLevel=999999;
	
	private String roleName;
	
	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	/**
	 * 用户角色
	 */
	private Set<Role> roles=new HashSet<Role>();
	
	/**
	 * 盐
	 */
	private String salt;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@ManyToMany
    @JoinTable(
            name="CMP_USER_ROLE",
            joinColumns=@JoinColumn(name="USER_ID"),
            inverseJoinColumns=@JoinColumn(name="ROLE_ID")
    )
	@JsonIgnore
	public Set<Role> getRoles() {
		return roles;
	}


	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
		if(this.roles!=null&&this.roles.size()>0){
			this.roleName="";
			for(Role role:roles){
				this.roleName+=role.getRoleName()+";";
			}
		}
	}
	
	@Transient 
	public String getCredentialsSalt() {
        return userName + salt;
    }
	
	/**
	 * @return the roleName
	 */
	@Transient
	public String getRoleName() {

		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	




	
}
