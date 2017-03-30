package com.springtest.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springtest.domain.UserInfo;
import com.springtest.service.IUserInfoService;

@Controller
public class UserInfoController {

	@Resource
	private IUserInfoService userInfoService;
	private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@RequestMapping("/showUser")
	public String toIndex(HttpServletRequest request, Model model) {
		logger.error("½øÈëµ½showUser.....");
		System.out.println("UserController showUser");
		int id = Integer.parseInt(request.getParameter("userId"));
		UserInfo user = userInfoService.getUserById(1);
		System.out.println(user.getUsername());
		model.addAttribute("user", user);
		return "/jsp/test";
	}
}
