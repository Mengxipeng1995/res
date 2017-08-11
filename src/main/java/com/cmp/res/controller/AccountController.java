package com.cmp.res.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccountController {
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage() {
		if(SecurityUtils.getSubject().isAuthenticated()){
			return "index";
		}
        return "login";
    }
	
	/*只有当登陆报错了才会进来这个方法中来。若身份验证成功的话，会直接跳转到之前的访问地址或是successfulUrl去*/
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String submitLoginForm(HttpServletRequest request,
            Model model) {
        String errorClassName = (String) request
                .getAttribute("shiroLoginFailure");
        String authticationError = null;
        if (UnknownAccountException.class.getName().equals(errorClassName)) {
            authticationError = "用户名/密码错误";
        } else if (IncorrectCredentialsException.class.getName().equals(
                errorClassName)) {
            authticationError = "用户名/密码错误";
        } else if (errorClassName != null) {
            authticationError = "未知错误：" + errorClassName;
        }
        model.addAttribute("authticationError", authticationError);
        return showLoginPage();
    }
}
