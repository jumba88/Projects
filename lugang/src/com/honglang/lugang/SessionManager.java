package com.honglang.lugang;

import com.honglang.lugang.login.UserInfo;

public class SessionManager {

	public static SessionManager instance;
	private String username;
	private String tokene;
	private String usertype;
	private String city;
	private UserInfo Account;
	public static SessionManager getInstance(){
		if(instance == null){
			instance = new SessionManager();
		}
		return instance;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTokene() {
		return tokene;
	}
	public void setTokene(String tokene) {
		this.tokene = tokene;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public UserInfo getAccount() {
		return Account;
	}
	public void setAccount(UserInfo account) {
		Account = account;
	}
}
