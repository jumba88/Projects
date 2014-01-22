package com.honglang.lugang.login;

import java.io.Serializable;

public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5384132524472398149L;

	private String username;
	private String token;
	private String usertype;
	private String province;
	private String city;
	private String county;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
}
