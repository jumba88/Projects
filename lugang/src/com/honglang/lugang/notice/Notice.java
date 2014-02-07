package com.honglang.lugang.notice;

import java.io.Serializable;

public class Notice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2837344640967013880L;

	private String title;
	private String content;
	private String addtime;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
}
