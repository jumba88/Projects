package com.honglang.lugang.company;

import java.io.Serializable;

public class Company implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3101019484768317091L;

	private String name;
	private String phone;
	private String address;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
