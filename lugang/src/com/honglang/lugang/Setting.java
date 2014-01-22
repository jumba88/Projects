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
	
	/*保存用户名*/
	public void saveAccount(String account){
		editor.putString("account", account);
		editor.commit();
	}
	/*获取保存在本地的用户名*/
	public String getAccount(){
		return sp.getString("account", null);
	}
	/*保存密码*/
	public void savePwd(String pwd){
		editor.putString("password", pwd);
		editor.commit();
	}
	/*获取保存在本地的密码*/
	public String getPwd(){
		return sp.getString("password", null);
	}
}
