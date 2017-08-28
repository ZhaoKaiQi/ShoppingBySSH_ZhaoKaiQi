package com.qf.shopping.annotation;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.qf.shopping.user.entity.User;

@Aspect
public class AOP {
	@Pointcut("execution(* com.qf.shopping.user.dao.UserDao.*.*(..))")
	public void pt() {
		// 这是一个没有任何实现的方法
	}

	@Before("pt()")
	public void setSession() {
		Session session = new Configuration().configure().addClass(User.class)
				.buildSessionFactory().openSession();
	}

	@After("pt()")
	public void transtion() {

	}
}
