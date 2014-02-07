package com.honglang.lugang.truck;

import java.io.Serializable;

public class Truck implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2223628690488797889L;

	private String chep;
	private String chex;
	private String pingp;
	private String rongl;
	private String startaddr;
	private String endaddr;
	private String price;
	private String realname;
	private String phone;
	private String remark;
	private String adddate;
	public String getChep() {
		return chep;
	}
	public void setChep(String chep) {
		this.chep = chep;
	}
	public String getChex() {
		return chex;
	}
	public void setChex(String chex) {
		this.chex = chex;
	}
	public String getPingp() {
		return pingp;
	}
	public void setPingp(String pingp) {
		this.pingp = pingp;
	}
	public String getRongl() {
		return rongl;
	}
	public void setRongl(String rongl) {
		this.rongl = rongl;
	}
	public String getStartaddr() {
		return startaddr;
	}
	public void setStartaddr(String startaddr) {
		this.startaddr = startaddr;
	}
	public String getEndaddr() {
		return endaddr;
	}
	public void setEndaddr(String endaddr) {
		this.endaddr = endaddr;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdddate() {
		return adddate;
	}
	public void setAdddate(String adddate) {
		this.adddate = adddate;
	}
}
