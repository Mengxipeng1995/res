package com.cmp.res.shiro;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmp.res.entity.ApplayResourceMapping;
import com.cmp.res.entity.Apply;
import com.cmp.res.entity.Menu;
import com.cmp.res.entity.Resource;
import com.cmp.res.entity.Role;
import com.cmp.res.entity.User;
import com.cmp.res.service.ApplayResourceMappingService;
import com.cmp.res.service.ApplyService;
import com.cmp.res.service.Log4jDBAppender;
import com.cmp.res.service.PasswordHelper;
import com.cmp.res.service.UserService;
import com.cmp.res.util.Log4jUtils;
import com.cmp.res.util.SystemConstant;
import com.google.common.base.Objects;




public class ShiroDBRealm extends AuthorizingRealm{
	
	
	@Autowired
	private PasswordHelper passwordHelper;
	
	public static Logger logger = LoggerFactory.getLogger(ShiroDBRealm.class);
	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}
	 public void clearCachedAuthenticationInfo(String principal) {
	        // TODO Auto-generated method stub
	        SimplePrincipalCollection principals = new SimplePrincipalCollection(
	                principal, getName());
	        clearCachedAuthenticationInfo(principals);
	    }
	 /**
	     * 清除所有用户授权信息缓存.
	     */
	    public void clearAllCachedAuthorizationInfo() {
	        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
	        if (cache != null) {
	            for (Object key : cache.keys()) {
	                cache.remove(key);
	            }
	        }
	    }

//	public void clearAllCachedAuthorizationInfo() {
//		getAuthorizationCache().clear();
//	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}
	
	@Autowired
	private UserService userService;
	@Autowired
	private ApplyService applyService;
	@Autowired
	private ApplayResourceMappingService applayResourceMappingService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();  
		ShiroUser shiroUser=  (ShiroUser)principals.getPrimaryPrincipal();
		User user=userService.getUserByName(shiroUser.getUserName());
		if(user!=null){
			
			Set<Role> roles=user.getRoles();
			for(Role role:roles){
				authorizationInfo.addRole(role.getRoleName());
				//菜单访问权限
				for(Menu menu:role.getMenus()){
					authorizationInfo.addStringPermission(menu.getUniqueIdentifierString());
				}
				//资源操作权限
				for(Resource resource:role.getResources()){
					authorizationInfo.addStringPermission(resource.getUniqueIdentifierString());
					
				}
			}
			
			
			//临时申请开通的权限      只查申请
			List<Apply> applys=applyService.list(user.getUserName(), 1);
			if(applys!=null&&applys.size()>0){
				for(Apply apply:applys){
					List<ApplayResourceMapping> arms=applayResourceMappingService.findByApplayId(apply.getId());
					if(arms!=null&&arms.size()>0){
						for(ApplayResourceMapping arm:arms){
							authorizationInfo.addStringPermission(arm.getUniqueIdentifierString());
						}
					}
				}
			}

			
			
		}

        return authorizationInfo;  
	}

	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		
		// TODO Auto-generated method stub
		 UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		 User user=null;
		 if("admin".equals(token.getUsername())){
			 user=new User();
			 user.setUserName("admin");
			 user.setPassword(SystemConstant.password);
			 user.setNickName("系统管理员");
			 passwordHelper.encryptPassword(user);
		 }else{
			 user=userService.getUserByName(token.getUsername());
		 }
		

	
		 if (user != null) {
			 StringBuilder sb=new StringBuilder("");
			 
			 SimpleAuthenticationInfo ac = new SimpleAuthenticationInfo(
					 new ShiroUser(user.getId(), user.getUserName(),
								user.getNickName(),sb.toString()), 
		                user.getPassword(), 
		                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
		                getName() 
		        );
			 
			 logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
						user.getUserName(), 0, user.getId(), user.getNickName()) ,
						Log4jDBAppender.EXTENT_MSG_DIVIDER,"用户登录");
		      return ac;
		    } else {
		      return null;
		    }
	}
	
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		public Long userId;
		public String userName;
		public String nickName;
		public String dept;

		public ShiroUser(Long userId, String userName, String nickName,String dept) {
			this.userId=userId;
			this.nickName=nickName;
			this.userName=userName;
			this.dept=dept;
		}
		

		

		/**
		 * @return the userId
		 */
		public Long getUserId() {
			return userId;
		}




		/**
		 * @param userId the userId to set
		 */
		public void setUserId(Long userId) {
			this.userId = userId;
		}




		public String getDept() {
			return dept;
		}




		public void setDept(String dept) {
			this.dept = dept;
		}




		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}




		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}




		/**
		 * @return the nickName
		 */
		public String getNickName() {
			return nickName;
		}




		/**
		 * @param nickName the nickName to set
		 */
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}




		/**
		 * �������������ΪĬ�ϵ�<shiro:principal/>���.
		 */
		@Override
		public String toString() {
			return userName;
		}

		/**
		 * ����hashCode,ֻ����loginName;
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(userName);
		}

		/**
		 * ����equals,ֻ����loginName;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (userName == null) {
				if (other.userName != null) {
					return false;
				}
			} else if (!userName.equals(other.userName)) {
				return false;
			}
			return true;
		}
	}

}
