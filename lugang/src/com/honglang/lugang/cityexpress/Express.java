package com.honglang.lugang.cityexpress;

import java.io.Serializable;

public class Express implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8314880531306455904L;

	private String id;
	private String fromcity;
	private String tocity;
	private String fromaddress;
	private String toaddress;
	private String minprice;
	private String lightprice;
	private String haevyprice;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFromcity() {
		return fromcity;
	}
	public void setFromcity(String fromcity) {
		this.fromcity = fromcity;
	}
	public String getTocity() {
		return tocity;
	}
	public void setTocity(String tocity) {
		this.tocity = tocity;
	}
	public String getFromaddress() {
		return fromaddress;
	}
	public void setFromaddress(String fromaddress) {
		this.fromaddress = fromaddress;
	}
	public String getToaddress() {
		return toaddress;
	}
	public void setToaddress(String toaddress) {
		this.toaddress = toaddress;
	}
	public String getMinprice() {
		return minprice;
	}
	public void setMinprice(String minprice) {
		this.minprice = minprice;
	}
	public String getLightprice() {
		return lightprice;
	}
	public void setLightprice(String lightprice) {
		this.lightprice = lightprice;
	}
	public String getHaevyprice() {
		return haevyprice;
	}
	public void setHaevyprice(String haevyprice) {
		this.haevyprice = haevyprice;
	}
}
