package com.honglang.lugang.login;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.home.HomeActivity;
import com.honglang.lugang.ui.ClearEditText;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class LoginActivity extends Activity {

	private static final String loginAction = "Login";
	private ClearEditText username;
	private ClearEditText password;
	private String userNo;
	private String pass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
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
//			this.startActivity(new Intent(this, HomeActivity.class));
//			this.finish();
		}
	}
	
	class LoginTask extends AsyncTask<String, String, Boolean>{

		private String loginResult;
		@Override
		protected Boolean doInBackground(String... params) {
			loginResult = login(params[0], params[1]);
			Log.i("Login", "LoginResult = " + loginResult);
			return true;
		}
		
	}
	
	private String login(String userNo, String pass){
		//指定WebService的命名空间和调用方法
		SoapObject request = new SoapObject(Constant.NAMESPACE, loginAction);
		//设置需要调用WebService接口的参数
		request.addProperty("userNO", userNo);
		request.addProperty("pass", pass);
		//获得序列化的Envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
		transport.debug = true;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		
		try {
			transport.call(Constant.NAMESPACE + loginAction, envelope);
			SoapObject obj = (SoapObject) envelope.bodyIn;
			if(obj != null){
				return obj.toString();
			}else{
				return null;
			}
			
		} catch (HttpResponseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

}
