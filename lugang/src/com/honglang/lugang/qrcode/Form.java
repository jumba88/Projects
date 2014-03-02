package com.honglang.lugang.qrcode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Form implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 202308466007475052L;

	private Info info;
	private List<HashMap<String, String>> stuff;
	public Info getInfo() {
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
	public List<HashMap<String, String>> getStuff() {
		return stuff;
	}
	public void setStuff(List<HashMap<String, String>> stuff) {
		this.stuff = stuff;
	}
}
