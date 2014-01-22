package com.honglang.lugang.cityexpress;

import java.io.Serializable;

public class Express implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8314880531306455904L;

	private String goal;
	private String price;
	private String light;
	private String heavy;
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLight() {
		return light;
	}
	public void setLight(String light) {
		this.light = light;
	}
	public String getHeavy() {
		return heavy;
	}
	public void setHeavy(String heavy) {
		this.heavy = heavy;
	}
}
