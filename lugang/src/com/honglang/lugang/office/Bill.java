package com.honglang.lugang.office;

import java.io.Serializable;

public class Bill implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7031077966715377323L;

	public String bill;
	public String time;
	public String code;
	public String sender;
	public String getter;
	public String getBill() {
		return bill;
	}
	public void setBill(String bill) {
		this.bill = bill;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getGetter() {
		return getter;
	}
	public void setGetter(String getter) {
		this.getter = getter;
	}
	
}
