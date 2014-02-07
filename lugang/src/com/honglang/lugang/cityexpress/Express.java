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
	private String details;
	private String wly_name;
	private String wly_phone;
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getWly_name() {
		return wly_name;
	}
	public void setWly_name(String wly_name) {
		this.wly_name = wly_name;
	}
	public String getWly_phone() {
		return wly_phone;
	}
	public void setWly_phone(String wly_phone) {
		this.wly_phone = wly_phone;
	}
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
