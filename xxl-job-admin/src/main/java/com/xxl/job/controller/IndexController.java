package com.xxl.job.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.job.controller.annotation.PermessionLimit;
import com.xxl.job.controller.interceptor.PermissionInterceptor;
import com.xxl.job.core.model.ReturnT;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
public class IndexController {

	@RequestMapping("/")
	@PermessionLimit(limit=false)
	public String index(Model model, HttpServletRequest request) {
		if (!PermissionInterceptor.ifLogin(request)) {
			return "redirect:/toLogin";
		}
		return "redirect:/jobinfo";
	}
	
	@RequestMapping("/toLogin")
	@PermessionLimit(limit=false)
	public String toLogin(Model model, HttpServletRequest request) {
		if (PermissionInterceptor.ifLogin(request)) {
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password){
		if (!PermissionInterceptor.ifLogin(request)) {
			if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)
					&& "admin".equals(userName) && "123456".equals(password)) {
				PermissionInterceptor.login(response);
			} else {
				return new ReturnT<String>(500, "账号或密码错误");
			}
		}
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
		if (PermissionInterceptor.ifLogin(request)) {
			PermissionInterceptor.logout(request, response);
		}
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping("/help")
	public String help(Model model) {
		return "help";
	}
	
}
