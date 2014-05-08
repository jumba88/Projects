package com.honglang.lugang.login;


import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.Setting;
import com.honglang.lugang.home.HomeActivity;
import com.honglang.lugang.qrcode.StuffActivity;
import com.honglang.lugang.ui.ClearEditText;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class LoginActivity extends Activity {

	private Setting setting;
	private String account;
	private ClearEditText username;
	private ClearEditText password;
	private String userNo;
	private String pass;
	private ProgressDialog progress;
	
	private static int DIR;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		DIR = getIntent().getIntExtra("dir", 1);
		
		setting = new Setting(this);
		
		username = (ClearEditText) this.findViewById(R.id.username);
		password = (ClearEditText) this.findViewById(R.id.password);
		
		account = setting.getAccount();
		if(account != null){
			username.setText(account);
			password.setText(setting.getPwd());
		}
	}

	public void onLogin(View v){
		userNo = username.getText().toString().trim();
		pass = password.getText().toString().trim();
		Calendar c = Calendar.getInstance();
		if (c.get(Calendar.MONTH) > 4) {
			Toast.makeText(this, "测试版本过期,请安装新版本", Toast.LENGTH_LONG).show();
			return;
		}
		if(TextUtils.isEmpty(userNo)){
			username.setShakeAnimation();
			return;
		}
		if(TextUtils.isEmpty(pass)){
			password.setShakeAnimation();
			return;
		}
		if(!TextUtils.isEmpty(userNo) && !TextUtils.isEmpty(pass)){
			new LoginTask().execute((Void)null);
//			Intent i = new Intent(LoginActivity.this, StuffActivity.class);
//			i.putExtra("action", 100);
//			LoginActivity.this.startActivity(i);
//			LoginActivity.this.finish();
		}
	}
	
	class LoginTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(LoginActivity.this, null, "登录中...");
			progress.setCancelable(false);
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			
			SoapObject request = new SoapObject(Constant.NAMESPACE, "Login");
			request.addProperty("userNO", userNo);
			request.addProperty("pass", pass);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			
				try {
					transport.call(Constant.NAMESPACE + "Login", envelope);
					SoapObject obj = (SoapObject) envelope.bodyIn;
					if(obj != null){
						JSONTokener jsonParser = new JSONTokener(obj.getPropertyAsString("LoginResult"));
						JSONObject json = (JSONObject) jsonParser.nextValue();
						if (json.getBoolean("result")) {
							
							JSONObject data = json.getJSONObject("data");
							SessionManager.getInstance().setUsername(userNo);
							SessionManager.getInstance().setTokene(data.getString("token"));
							SessionManager.getInstance().setCity(data.getString("city"));
							SessionManager.getInstance().setUsertype(data.getString("usertype"));
							return true;
						}else{
							errMsg = json.getString("msg");
							return false;
						}
					}
					
				} catch (Exception e) {
//					errMsg = e.toString();
					e.printStackTrace();
					errMsg = "请检查网络连接是否正常";
				}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if(result){
				if (DIR == 0) {
					LoginActivity.this.startActivity(new Intent(LoginActivity.this, HomeActivity.class));
				}
				
				LoginActivity.this.finish();
			}else {
				Toast.makeText(LoginActivity.this, "登录失败," + errMsg, Toast.LENGTH_LONG).show();
			}
			
			super.onPostExecute(result);
		}
		
	}
	

}
