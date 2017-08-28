package com.qf.shopping.user.dao;

import java.sql.SQLException;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.qf.shopping.user.entity.User;

@Component
public class UserDao implements IUserDao {
	@Resource
	User user = null;
	@Resource
	SessionFactory sessionFactory = null;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Resource
	JdbcTemplate jdbcTemplate = null;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void save(User user) {
		sessionFactory.openSession().save(user);
		/*
		 * QueryRunner runner = JDBCUtils.getQueryRunner(); String sql =
		 * "insert into t_user(uName,uPassword,uCode,uState) values(?,?,?,?)";
		 * try { runner.update(sql, user.getuName(), user.getuPassword(),
		 * user.getuCode(), user.getuState()); } catch (SQLException e) { throw
		 * new RuntimeException(e); }
		 */
	}

	@Override
	public boolean findUserNameExitOrNot(User user) {
		System.out.println("这个判断的方法执行了" + user);
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from User where uName=?");
		query.setParameter(0, user.getuName());
		List<User> users = query.list();
		System.out.println(users.size());
		if (users != null && users.size() > 0) {// 这个表示不合法的,数据已经存在了
			System.out.println("这个表示不合法的,数据已经存在了");
			return true;
		} else {
			System.out.println("这个表示合法的");
			return false;// 这个表示合法的
		}
	}

	// 查询数据库的问题
	@Override
	public User findUserByCode(String code) throws SQLException {
		System.out.println("我是激活查用户的方法，我执行了" + code);
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from User where uCode=?");
		query.setParameter(0, code);
		List<User> users = query.list();
		// 判定
		if (users.size() > 0) {
			return users.get(0);
		}
		throw new RuntimeException("数据为null");
	}

	@Override
	public void changeUState(int getuId) {

		try {
			jdbcTemplate.update("update t_user set uState=1 where uId=?",
					getuId);
			System.out.println("我执行了");
		} catch (Exception e) {
			throw new RuntimeException("更新用户状态失败");
		}
	}

	@Override
	public void clearCode(int getuId) {
		try {
			jdbcTemplate.update("update t_user set uCode='' where uId=?",
					getuId);
		} catch (Exception e) {
			throw new RuntimeException("清空激活码状态失败");
		}
	}

	@Override
	public User findPasswordByName(User user) throws SQLException {
		System.out.println("调用通过用户名查找密码的userdao层执行了");
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from User where uName=?");
		query.setParameter(0, user.getuName());
		List<User> list = query.list();
		return list.get(0);
	}

	@Override
	public User getUserByName(User user) throws SQLException {
		System.out.println("调用通过用户名查找密码的userdao层执行了");
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from User where uName=?");
		query.setParameter(0, user.getuName());
		List<User> users = query.list();
		// 判定
		if (users.size() > 0) {
			return users.get(0);
		}
		throw new RuntimeException("数据为null");
	}
}
