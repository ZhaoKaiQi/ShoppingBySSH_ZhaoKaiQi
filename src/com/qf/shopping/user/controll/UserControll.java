package com.qf.shopping.user.controll;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qf.shopping.user.entity.User;
import com.qf.shopping.user.service.IUserService;
import com.qf.shopping.utils.MsgUtils;
import com.qf.shopping.utils.ObjUtils;

@Component
@Controller
public class UserControll {
	// 注入service层
	@Resource
	IUserService userService = null;

	// @Resource
	// SessionFactory sessionFactory = null;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	// 下面这些是注册时验证的方法
	/**
	 * @Title: handleRegist
	 * @Description: 这是检验验证码是否正确以及确认登录的方法
	 * @param @param user
	 * @param @param request
	 * @param @param response
	 * @param @throws ServletException
	 * @param @throws IOException
	 * @return void
	 * @throws
	 */
	@RequestMapping("/finalCheck")
	public void handleRegist(User user, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("regist页面传过来的方法可以执行");
		System.out.println("这个是提交时的user对象：" + user);
		// 获取验证码
		String yzm = request.getParameter("yzm");
		// 从session的域对象中取出验证码
		String checkCode = (String) request.getSession().getAttribute(
				"checkcode");
		System.out.println("这个就是提交的时候的验证码：" + checkCode);
		if (yzm.equalsIgnoreCase(checkCode)) {
			// 实际开发中首先做后台校验
			boolean b = userService.checkUserNameValitedOrNot(user);
			if (!b) {
				// 如果用户注册
				user.setuCode(UUID.randomUUID().toString().replace("-", ""));// 通过UUID来生成用户的码值
				user.setuState(0);
				userService.regist(user);
				System.out.println("发送邮件前最后的user状态：" + user);
				// 这里需要向客户端发送邮件
				try {
					MsgUtils.sendMsg("boy@qiqi.com", user.getuCode());
				} catch (Exception e) {
					throw new RuntimeException("邮件发送出现异常：" + e);
				}
				// 下面应该跳转到注册成功的页面中去
				request.getRequestDispatcher("/WEB-INF/zhucexiangxi.jsp")
						.forward(request, response);
			}
		} else {
			// 告诉用户验证码有问题
			request.setAttribute("checkCode", "验证码有问题");
			request.getRequestDispatcher("/WEB-INF/regist.jsp").forward(
					request, response);
		}
	}

	/**
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @Title: checkUserValitedAndNot
	 * @Description: 检验用户的合法性
	 * @param @param response
	 * @param @param user
	 * @param @throws IOException
	 * @return void
	 * @throws
	 */
	@RequestMapping("/checkUserName")
	public void checkUserValitedAndNot(HttpServletRequest request,
			HttpServletResponse response, User user) throws IOException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		user = ObjUtils.getParametes(request, User.class);
		System.out.println("执行过checkUserName这个方法了，传进的用户为：" + user);
		boolean b = userService.checkUserNameValitedOrNot(user);
		System.out.println("判断用户名是否合法" + b);
		// 我们返回不合法的数据
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		if (b) {
			// 写数据
			writer.write("<font color='red'>用户已经存在了</font>");
		} else {
			// 写数据
			writer.write("<font color='green'>用户名是合法的</font>");
		}
		writer.flush();
		writer.close();
	}

	// 这个是用来激活用户的
	/**
	 * @Title: handleCode
	 * @Description: 这个方法是用来激活验证码的
	 * @param @param request
	 * @param @param response
	 * @param @throws SQLException
	 * @return void
	 * @throws
	 */
	@RequestMapping("/giveLife")
	public void handleCode(HttpServletRequest request,
			HttpServletResponse response) throws SQLException {
		String code = request.getParameter("checkcode");
		System.out.println("这个是激活码，千万别再空了，求求你" + code);
		System.out.println("用户点击并激活了账户，执行了UserServlet中的handleCode方法！");
		// 查询数据库这个验证码是否存在，如果存在的话，将用户的状态设置成1
		User user = userService.checkCodeExitOrNot(code);
		// 判断
		if (user != null) {
			// 改变
			userService.changeuState(user.getuId());
			// 清空状态码
			userService.clearCode(user.getuId());
		}
	}

	// 下面的方法是用来登录验证及操作的
	/**
	 * @Title: checkLandingUserValitedAndNot
	 * @Description: 这个方法是用来校验用户名是否合法的
	 * @param @param response
	 * @param @param user
	 * @param @throws IOException
	 * @return void
	 * @throws
	 */
	@RequestMapping("/checkLandingUserName")
	public void checkLandingUserValitedAndNot(HttpServletResponse response,
			User user) throws ServletException, IOException {
		// 实际开发中首先做后台校验
		boolean b = userService.checkUserNameValitedOrNot(user);
		// 我们返回不合法的数据
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		if (!b) {
			// 写数据
			writer.write("<font color='red'>用户不存在</font>");
		} else {
			// 写数据
			writer.write("<font color='green'>该用户合法</font>");
		}
		writer.flush();
		writer.close();
	}

	/**
	 * @return
	 * @Title: finalLanding
	 * @Description: 这是登陆的方法
	 * @param @param request
	 * @param @param response
	 * @param @param user
	 * @param @throws ServletException
	 * @param @throws IOException
	 * @param @throws SQLException
	 * @return void
	 * @throws
	 */
	@RequestMapping("/finalCheckLanding")
	public String finalLanding(HttpServletRequest request,
			HttpServletResponse response, User user) throws ServletException,
			IOException, SQLException {
		System.out.println("landing传过来的方法可以执行");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		boolean hl = handleLanding(user, request, response);
		boolean clp = checkLandingpasswordRightOrWrongSon(request, response,
				user);
		if (hl && clp) {
			System.out.println("这是一个很重要的方法，必须执行");
			// 说明用户名、密码、验证码、状态值（已注册）都没问题
			request.getSession().setAttribute("user", user);
			response.setCharacterEncoding("user");
			return "/WEB-INF/index.jsp";
		} else {// 这里有问题，所以跳转回登陆页面
			System.out.println("这是一个很重要的方法，必须执行2");
			return "/WEB-INF/landing.jsp";
		}
	}

	/**
	 * @Title: handleLanding
	 * @Description: 处理登陆的方法
	 * @param @param user
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws ServletException
	 * @param @throws IOException
	 * @param @throws SQLException
	 * @return boolean
	 * @throws
	 */
	private boolean handleLanding(User user, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException {
		request.setCharacterEncoding("UTF-8");
		// 获取验证码
		String yzm = request.getParameter("yzm");
		// 从session的域对象中取出验证码
		String checkCode = (String) request.getSession().getAttribute(
				"checkcode");
		// 这里构造一个获取state的方法得到对应的状态值
		User str = userService.getUserByName(user);
		PrintWriter writer = response.getWriter();
		int uState = str.getuState();
		System.out.println("数据库中验证码的ustate值是：" + uState);
		if (yzm.equalsIgnoreCase(checkCode) && uState == 1) {
			System.out.println("执行到...");
			return true;
			// 这就说明已经注册激活可以登录跳转到主页
			// 下面应该跳转到登陆的页面中去
			// request.getRequestDispatcher("/index.do").forward(request,
			// response);
		} else {
			return false;
			// 告诉用户验证码有问题
			// request.setAttribute("checkCode",
			// "验证码有问题");request.getRequestDispatcher("/WEB-INF/landing.jsp").forward(request,
			// response);
		}
		// request.getRequestDispatcher("/WEB-INF/landing.jsp").forward(request,
		// response);
	}

	/**
	 * @return
	 * 
	 * @Title: zhuxiao
	 * @Description: 注销
	 * @param @param request
	 * @param @param response
	 * @param @param user
	 * @param @throws ServletException
	 * @param @throws IOException
	 * @param @throws SQLException
	 * @return void
	 * @throws
	 */
	@RequestMapping("/distroy")
	public String zhuxiao(HttpSession session, User user)
			throws ServletException, IOException, SQLException {
		//销毁session
		session.invalidate();
		return "redirect:index.action";
	}

	/**
	 * @Title: checkLandingpasswordRightOrWrong
	 * @Description: 用来校验密码是否合法的方法
	 * @param @param request
	 * @param @param response
	 * @param @param user
	 * @param @return
	 * @param @throws IOException
	 * @param @throws SQLException
	 * @param @throws ServletException
	 * @return boolean
	 * @throws
	 */
	@RequestMapping("/checkLandingPassword")
	public void checkLandingpasswordRightOrWrong(HttpServletRequest request,
			HttpServletResponse response, User user) throws IOException,
			SQLException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		boolean b = userService.checkUserNameValitedOrNot(user);
		PrintWriter writer = response.getWriter();

		System.out.println("我是验证密码是否合法的方法，哈哈哈哈");
		System.out.println(b);
		System.out.println(user.getuName());
		System.out.println(user.getuPassword());
		if (b) {
			User pwd = userService.findUserPasswordByName(user);
			System.out.println("数据库中记录的密码是：" + pwd.getuPassword());
			boolean c = (user.getuPassword()).equals(pwd.getuPassword());
			System.out.println("第二层判定中c的值：" + c);
			if (!c) {
				// 用户名存在的情况，校验数据库中的相应密码是否和页面传进来的一样
				writer.write("<font color='red'>密码错误</font>");
				System.out.println("应该打印出密码是错误的！");
				writer.flush();
				writer.close();
			} else {
				writer.write("<font color='green'>密码正确</font>");
				writer.flush();
				writer.close();
			}
		} else {
			// 用户名不存在的情况下，直接输出“用户不存在”就OK
			writer.write("<font color='red'>用户不存在</font>");
			System.out.println("应该打印用户不存在！");
			writer.flush();
			writer.close();
		}
	}

	private boolean checkLandingpasswordRightOrWrongSon(
			HttpServletRequest request, HttpServletResponse response, User user)
			throws SQLException {
		boolean b = userService.checkUserNameValitedOrNot(user);
		if (b) {
			User pwd = userService.findUserPasswordByName(user);
			System.out.println("数据库中记录的密码是：" + pwd.getuPassword());
			boolean c = (user.getuPassword()).equals(pwd.getuPassword());
			System.out.println("第二层判定中c的值：" + c);
			if (!c) {
				// 用户名存在的情况，校验数据库中的相应密码是否和页面传进来的一样
				System.out.println("应该打印出密码是错误的！");
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

}