package com.honglang.lugang.login;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.Setting;
import com.honglang.lugang.home.HomeActivity;
import com.honglang.lugang.ui.ClearEditText;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class LoginActivity extends Activity {

	private Setting setting;
	private ClearEditText username;
	private ClearEditText password;
	private String userNo;
	private String pass;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setting = new Setting(this);
		
		username = (ClearEditText) this.findViewById(R.id.username);
		password = (ClearEditText) this.findViewById(R.id.password);
		
	}

	public void onLogin(View v){
		userNo = username.getText().toString().trim();
		pass = password.getText().toString().trim();
		if(TextUtils.isEmpty(userNo)){
			username.setShakeAnimation();
			return;
		}
		if(TextUtils.isEmpty(pass)){
			password.setShakeAnimation();
			return;
		}
		if(!TextUtils.isEmpty(userNo) && !TextUtils.isEmpty(pass)){
			new LoginTask().execute(userNo,pass);
			
		}
	}
	
	class LoginTask extends AsyncTask<String, String, Boolean>{

		private String loginResult;
		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(LoginActivity.this, null, "登录中...");
			progress.setCancelable(false);
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(String... params) {
			loginResult = Constant.login(params[0], params[1]);
//			Log.i("Login", "" + loginResult);
			JSONTokener jsonParser = new JSONTokener(loginResult);
			if (jsonParser != null) {
				try {
					JSONObject json = (JSONObject) jsonParser.nextValue();
					if (json.getBoolean("result")) {
						//记住用户
						setting.saveAccount(params[0]);
						setting.savePwd(params[1]);
						SessionManager.getInstance().setUsername(params[0]);
						
						JSONObject data = json.getJSONObject("data");
						SessionManager.getInstance().setTokene(data.getString("token"));
						SessionManager.getInstance().setCity(data.getString("city"));
						SessionManager.getInstance().setUsertype(data.getString("usertype"));
						return true;
					}else{
						errMsg = json.getString("msg");
						return false;
					}
				} catch (JSONException e) {
					errMsg = e.toString();
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				LoginActivity.this.startActivity(new Intent(LoginActivity.this, HomeActivity.class));
				LoginActivity.this.finish();
			}else {
				Toast.makeText(LoginActivity.this, errMsg, Toast.LENGTH_LONG).show();
			}
			progress.dismiss();
			super.onPostExecute(result);
		}
		
	}
	

}
