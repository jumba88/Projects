package com.honglang.lugang.company;

import java.io.Serializable;

public class Company implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3101019484768317091L;

	private String no;
	private String fk_station;
	private String name;
	private String qyjingyinfanwei;
	private String phone;
	private String address;
	private String qiyexinyu;
	private String qywanlaixianlu;
	private String jychuanz;
	public String getQywanlaixianlu() {
		return qywanlaixianlu;
	}
	public void setQywanlaixianlu(String qywanlaixianlu) {
		this.qywanlaixianlu = qywanlaixianlu;
	}
	public String getJychuanz() {
		return jychuanz;
	}
	public void setJychuanz(String jychuanz) {
		this.jychuanz = jychuanz;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getFk_station() {
		return fk_station;
	}
	public void setFk_station(String fk_station) {
		this.fk_station = fk_station;
	}
	public String getQyjingyinfanwei() {
		return qyjingyinfanwei;
	}
	public void setQyjingyinfanwei(String qyjingyinfanwei) {
		this.qyjingyinfanwei = qyjingyinfanwei;
	}
	public String getQiyexinyu() {
		return qiyexinyu;
	}
	public void setQiyexinyu(String qiyexinyu) {
		this.qiyexinyu = qiyexinyu;
	}
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
