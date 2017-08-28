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
	// ע��service��
	@Resource
	IUserService userService = null;

	// @Resource
	// SessionFactory sessionFactory = null;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	// ������Щ��ע��ʱ��֤�ķ���
	/**
	 * @Title: handleRegist
	 * @Description: ���Ǽ�����֤���Ƿ���ȷ�Լ�ȷ�ϵ�¼�ķ���
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
		System.out.println("registҳ�洫�����ķ�������ִ��");
		System.out.println("������ύʱ��user����" + user);
		// ��ȡ��֤��
		String yzm = request.getParameter("yzm");
		// ��session���������ȡ����֤��
		String checkCode = (String) request.getSession().getAttribute(
				"checkcode");
		System.out.println("��������ύ��ʱ�����֤�룺" + checkCode);
		if (yzm.equalsIgnoreCase(checkCode)) {
			// ʵ�ʿ�������������̨У��
			boolean b = userService.checkUserNameValitedOrNot(user);
			if (!b) {
				// ����û�ע��
				user.setuCode(UUID.randomUUID().toString().replace("-", ""));// ͨ��UUID�������û�����ֵ
				user.setuState(0);
				userService.regist(user);
				System.out.println("�����ʼ�ǰ����user״̬��" + user);
				// ������Ҫ��ͻ��˷����ʼ�
				try {
					MsgUtils.sendMsg("boy@qiqi.com", user.getuCode());
				} catch (Exception e) {
					throw new RuntimeException("�ʼ����ͳ����쳣��" + e);
				}
				// ����Ӧ����ת��ע��ɹ���ҳ����ȥ
				request.getRequestDispatcher("/WEB-INF/zhucexiangxi.jsp")
						.forward(request, response);
			}
		} else {
			// �����û���֤��������
			request.setAttribute("checkCode", "��֤��������");
			request.getRequestDispatcher("/WEB-INF/regist.jsp").forward(
					request, response);
		}
	}

	/**
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @Title: checkUserValitedAndNot
	 * @Description: �����û��ĺϷ���
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
		System.out.println("ִ�й�checkUserName��������ˣ��������û�Ϊ��" + user);
		boolean b = userService.checkUserNameValitedOrNot(user);
		System.out.println("�ж��û����Ƿ�Ϸ�" + b);
		// ���Ƿ��ز��Ϸ�������
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		if (b) {
			// д����
			writer.write("<font color='red'>�û��Ѿ�������</font>");
		} else {
			// д����
			writer.write("<font color='green'>�û����ǺϷ���</font>");
		}
		writer.flush();
		writer.close();
	}

	// ��������������û���
	/**
	 * @Title: handleCode
	 * @Description: �������������������֤���
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
		System.out.println("����Ǽ����룬ǧ����ٿ��ˣ�������" + code);
		System.out.println("�û�������������˻���ִ����UserServlet�е�handleCode������");
		// ��ѯ���ݿ������֤���Ƿ���ڣ�������ڵĻ������û���״̬���ó�1
		User user = userService.checkCodeExitOrNot(code);
		// �ж�
		if (user != null) {
			// �ı�
			userService.changeuState(user.getuId());
			// ���״̬��
			userService.clearCode(user.getuId());
		}
	}

	// ����ķ�����������¼��֤��������
	/**
	 * @Title: checkLandingUserValitedAndNot
	 * @Description: �������������У���û����Ƿ�Ϸ���
	 * @param @param response
	 * @param @param user
	 * @param @throws IOException
	 * @return void
	 * @throws
	 */
	@RequestMapping("/checkLandingUserName")
	public void checkLandingUserValitedAndNot(HttpServletResponse response,
			User user) throws ServletException, IOException {
		// ʵ�ʿ�������������̨У��
		boolean b = userService.checkUserNameValitedOrNot(user);
		// ���Ƿ��ز��Ϸ�������
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		if (!b) {
			// д����
			writer.write("<font color='red'>�û�������</font>");
		} else {
			// д����
			writer.write("<font color='green'>���û��Ϸ�</font>");
		}
		writer.flush();
		writer.close();
	}

	/**
	 * @return
	 * @Title: finalLanding
	 * @Description: ���ǵ�½�ķ���
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
		System.out.println("landing�������ķ�������ִ��");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		boolean hl = handleLanding(user, request, response);
		boolean clp = checkLandingpasswordRightOrWrongSon(request, response,
				user);
		if (hl && clp) {
			System.out.println("����һ������Ҫ�ķ���������ִ��");
			// ˵���û��������롢��֤�롢״ֵ̬����ע�ᣩ��û����
			request.getSession().setAttribute("user", user);
			response.setCharacterEncoding("user");
			return "/WEB-INF/index.jsp";
		} else {// ���������⣬������ת�ص�½ҳ��
			System.out.println("����һ������Ҫ�ķ���������ִ��2");
			return "/WEB-INF/landing.jsp";
		}
	}

	/**
	 * @Title: handleLanding
	 * @Description: �����½�ķ���
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
		// ��ȡ��֤��
		String yzm = request.getParameter("yzm");
		// ��session���������ȡ����֤��
		String checkCode = (String) request.getSession().getAttribute(
				"checkcode");
		// ���ﹹ��һ����ȡstate�ķ����õ���Ӧ��״ֵ̬
		User str = userService.getUserByName(user);
		PrintWriter writer = response.getWriter();
		int uState = str.getuState();
		System.out.println("���ݿ�����֤���ustateֵ�ǣ�" + uState);
		if (yzm.equalsIgnoreCase(checkCode) && uState == 1) {
			System.out.println("ִ�е�...");
			return true;
			// ���˵���Ѿ�ע�ἤ����Ե�¼��ת����ҳ
			// ����Ӧ����ת����½��ҳ����ȥ
			// request.getRequestDispatcher("/index.do").forward(request,
			// response);
		} else {
			return false;
			// �����û���֤��������
			// request.setAttribute("checkCode",
			// "��֤��������");request.getRequestDispatcher("/WEB-INF/landing.jsp").forward(request,
			// response);
		}
		// request.getRequestDispatcher("/WEB-INF/landing.jsp").forward(request,
		// response);
	}

	/**
	 * @return
	 * 
	 * @Title: zhuxiao
	 * @Description: ע��
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
		//����session
		session.invalidate();
		return "redirect:index.action";
	}

	/**
	 * @Title: checkLandingpasswordRightOrWrong
	 * @Description: ����У�������Ƿ�Ϸ��ķ���
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

		System.out.println("������֤�����Ƿ�Ϸ��ķ�������������");
		System.out.println(b);
		System.out.println(user.getuName());
		System.out.println(user.getuPassword());
		if (b) {
			User pwd = userService.findUserPasswordByName(user);
			System.out.println("���ݿ��м�¼�������ǣ�" + pwd.getuPassword());
			boolean c = (user.getuPassword()).equals(pwd.getuPassword());
			System.out.println("�ڶ����ж���c��ֵ��" + c);
			if (!c) {
				// �û������ڵ������У�����ݿ��е���Ӧ�����Ƿ��ҳ�洫������һ��
				writer.write("<font color='red'>�������</font>");
				System.out.println("Ӧ�ô�ӡ�������Ǵ���ģ�");
				writer.flush();
				writer.close();
			} else {
				writer.write("<font color='green'>������ȷ</font>");
				writer.flush();
				writer.close();
			}
		} else {
			// �û��������ڵ�����£�ֱ��������û������ڡ���OK
			writer.write("<font color='red'>�û�������</font>");
			System.out.println("Ӧ�ô�ӡ�û������ڣ�");
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
			System.out.println("���ݿ��м�¼�������ǣ�" + pwd.getuPassword());
			boolean c = (user.getuPassword()).equals(pwd.getuPassword());
			System.out.println("�ڶ����ж���c��ֵ��" + c);
			if (!c) {
				// �û������ڵ������У�����ݿ��е���Ӧ�����Ƿ��ҳ�洫������һ��
				System.out.println("Ӧ�ô�ӡ�������Ǵ���ģ�");
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

}