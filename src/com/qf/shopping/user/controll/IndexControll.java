package com.qf.shopping.user.controll;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@Controller
public class IndexControll {
	@RequestMapping("/regist")
	public String turnRegist() {
		return "/WEB-INF/regist.jsp";
	}

	@RequestMapping("/landing")
	public String turnLanding() {
		return "/WEB-INF/landing.jsp";
	}

	@RequestMapping("/index")
	public String turnIndex() {
		return "/WEB-INF/index.jsp";
	}
}
