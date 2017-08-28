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
		System.out.println("����жϵķ���ִ����" + user);
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from User where uName=?");
		query.setParameter(0, user.getuName());
		List<User> users = query.list();
		System.out.println(users.size());
		if (users != null && users.size() > 0) {// �����ʾ���Ϸ���,�����Ѿ�������
			System.out.println("�����ʾ���Ϸ���,�����Ѿ�������");
			return true;
		} else {
			System.out.println("�����ʾ�Ϸ���");
			return false;// �����ʾ�Ϸ���
		}
	}

	// ��ѯ���ݿ������
	@Override
	public User findUserByCode(String code) throws SQLException {
		System.out.println("���Ǽ�����û��ķ�������ִ����" + code);
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from User where uCode=?");
		query.setParameter(0, code);
		List<User> users = query.list();
		// �ж�
		if (users.size() > 0) {
			return users.get(0);
		}
		throw new RuntimeException("����Ϊnull");
	}

	@Override
	public void changeUState(int getuId) {

		try {
			jdbcTemplate.update("update t_user set uState=1 where uId=?",
					getuId);
			System.out.println("��ִ����");
		} catch (Exception e) {
			throw new RuntimeException("�����û�״̬ʧ��");
		}
	}

	@Override
	public void clearCode(int getuId) {
		try {
			jdbcTemplate.update("update t_user set uCode='' where uId=?",
					getuId);
		} catch (Exception e) {
			throw new RuntimeException("��ռ�����״̬ʧ��");
		}
	}

	@Override
	public User findPasswordByName(User user) throws SQLException {
		System.out.println("����ͨ���û������������userdao��ִ����");
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from User where uName=?");
		query.setParameter(0, user.getuName());
		List<User> list = query.list();
		return list.get(0);
	}

	@Override
	public User getUserByName(User user) throws SQLException {
		System.out.println("����ͨ���û������������userdao��ִ����");
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from User where uName=?");
		query.setParameter(0, user.getuName());
		List<User> users = query.list();
		// �ж�
		if (users.size() > 0) {
			return users.get(0);
		}
		throw new RuntimeException("����Ϊnull");
	}
}
