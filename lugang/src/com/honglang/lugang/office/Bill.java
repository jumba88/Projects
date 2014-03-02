package com.honglang.lugang.office;

import java.io.Serializable;

public class Bill implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7031077966715377323L;

	public String title;
	public String trun_time;
	public String done_time;
	public String form_oid;
	public String current_node_id;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTrun_time() {
		return trun_time;
	}
	public void setTrun_time(String trun_time) {
		this.trun_time = trun_time;
	}
	public String getDone_time() {
		return done_time;
	}
	public void setDone_time(String done_time) {
		this.done_time = done_time;
	}
	public String getForm_oid() {
		return form_oid;
	}
	public void setForm_oid(String form_oid) {
		this.form_oid = form_oid;
	}
	public String getCurrent_node_id() {
		return current_node_id;
	}
	public void setCurrent_node_id(String current_node_id) {
		this.current_node_id = current_node_id;
	}
}
