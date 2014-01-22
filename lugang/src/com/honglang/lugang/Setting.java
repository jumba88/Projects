package com.honglang.lugang;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Setting {

	public Context mContext;
	public SharedPreferences sp;
	public SharedPreferences.Editor editor;
	
	public Setting(Context context) {
		super();
		this.mContext = context;
		sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		editor = sp.edit();
	}
	
	/*�����û���*/
	public void saveAccount(String account){
		editor.putString("account", account);
		editor.commit();
	}
	/*��ȡ�����ڱ��ص��û���*/
	public String getAccount(){
		return sp.getString("account", null);
	}
	/*��������*/
	public void savePwd(String pwd){
		editor.putString("password", pwd);
		editor.commit();
	}
	/*��ȡ�����ڱ��ص�����*/
	public String getPwd(){
		return sp.getString("password", null);
	}
}
