package com.qf.shopping.user.entity;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

@Component
public class User {
	private int uId;// 编号
	private String uName;// 用户名
	private String uPassword;// 密码
	private String uCode;// 激活码
	private int uState;// 状态：0未激活，1激活了

	// 设置属性的setter and getter 方法
	

	// 重写toString方法
	@Override
	public String toString() {
		return "User [uId=" + uId + ", uName=" + uName + ", uPassword="
				+ uPassword + ", uCode=" + uCode + ", uState=" + uState + "]";
	}

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getuPassword() {
		return uPassword;
	}

	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}

	public String getuCode() {
		return uCode;
	}

	public void setuCode(String uCode) {
		this.uCode = uCode;
	}

	public int getuState() {
		return uState;
	}

	public void setuState(int uState) {
		this.uState = uState;
	}
}
