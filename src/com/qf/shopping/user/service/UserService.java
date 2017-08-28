package com.qf.shopping.user.service;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.qf.shopping.user.dao.IUserDao;
import com.qf.shopping.user.entity.User;

@Component
public class UserService implements IUserService {
	@Resource
	IUserDao userDao = null;

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void regist(User user) {
		userDao.save(user);
	}

	@Override
	public boolean checkUserNameValitedOrNot(User user) {
		if (null == user.getuName() || "".equals(user.getuName())) {
			throw new RuntimeException("�û���Ϊ��...");
		} else {
			return userDao.findUserNameExitOrNot(user);
		}
	}

	@Override
	public User checkCodeExitOrNot(String code) {
		if (null == code || "".equals(code)) {
			throw new RuntimeException("������Ϊ�գ�");
		}
		try {
			return userDao.findUserByCode(code);
		} catch (SQLException e) {
			throw new RuntimeException("�������������");
		}
	}

	@Override
	public void changeuState(int getuId) {
		if (getuId < 0) {
			throw new RuntimeException("�û�IDΪ�գ��޷��ı��û�״̬");
		}
		userDao.changeUState(getuId);
	}

	@Override
	public void clearCode(int getuId) {
		if (getuId < 0) {
			throw new RuntimeException("�û�IDΪ�գ��޷��ı��û�״̬");
		}
		userDao.clearCode(getuId);
	}

	@Override
	public User findUserPasswordByName(User user) throws SQLException {
		if (null == user.getuName() || "".equals(user.getuName())) {
			throw new RuntimeException("�û���Ϊ��...");
		}
		return userDao.findPasswordByName(user);
	}

	@Override
	public User getUserByName(User user) throws SQLException {
		if (null == user.getuName() || "".equals(user.getuName())) {
			throw new RuntimeException("�û���Ϊ��...");
		}
		return userDao.getUserByName(user);
	}
}