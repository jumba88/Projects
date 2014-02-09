package com.honglang.lugang.billsearch;

import java.io.Serializable;

public class Record implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7448945082575680230L;

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
