package com.honglang.lugang.truck;

import java.io.Serializable;

public class Truck implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2223628690488797889L;

	private String truck;
	private String location;
	private String park;
	private String update;
	public String getTruck() {
		return truck;
	}
	public void setTruck(String truck) {
		this.truck = truck;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPark() {
		return park;
	}
	public void setPark(String park) {
		this.park = park;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
}
