package com.honglang.lugang.assign;

import java.io.Serializable;

public class Assign implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String stuff;
	private String from;
	private String to;
	private String total;
	public String getStuff() {
		return stuff;
	}
	public void setStuff(String stuff) {
		this.stuff = stuff;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
}
