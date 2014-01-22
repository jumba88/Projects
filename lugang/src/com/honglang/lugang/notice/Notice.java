package com.honglang.lugang.notice;

import java.io.Serializable;

public class Notice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2837344640967013880L;

	private String notice;
	private String date;
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
