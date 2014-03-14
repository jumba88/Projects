package com.honglang.lugang.login;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.honglang.lugang.Constant;
import com.honglang.lugang.HlApp;
import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.SessionManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	
	private TextView no;
	private TextView question;
	private TextView answer;
	private TextView email;
	private TextView name;
	private TextView code;
	private TextView range;
	private TextView superior;
	private TextView address;
	private TextView postcode;
	private TextView legal;
	private TextView type;
	private TextView card;
	private TextView charger;
	private TextView tel;
	private TextView phone;
	private TextView mobile;
	private TextView credit;
	private TextView path;
	
	UserInfo account;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		title = (TextView) this.findViewById(R.id.title);
		title.setText("用户资料");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		new LoadTask().execute((Void)null);
//		init();
	}

	private void init(){
		no = (TextView) findViewById(R.id.no);
		no.setText(account.getNo()+"");
		question = (TextView) findViewById(R.id.question);
		question.setText(account.getPasstimu()+"");
		answer = (TextView) findViewById(R.id.answer);
		answer.setText(account.getPassanswer()+"");
		email = (TextView) findViewById(R.id.email);
		email.setText(account.getEmail()+"");
		name = (TextView) findViewById(R.id.name);
		name.setText(account.getName()+"");
		code = (TextView) findViewById(R.id.code);
		code.setText(account.getJgcode()+"");
		range = (TextView) findViewById(R.id.range);
		range.setText(account.getQyjingyinfanwei()+"");
		superior = (TextView) findViewById(R.id.superior);
		superior.setText(account.getPbussinssname()+"");
		address = (TextView) findViewById(R.id.address);
		address.setText(account.getAddress()+"");
		postcode = (TextView) findViewById(R.id.postcode);
		postcode.setText(account.getPostcode()+"");
		legal = (TextView) findViewById(R.id.legal);
		legal.setText(account.getFrname()+"");
		type = (TextView) findViewById(R.id.type);
		type.setText(account.getFrzjtype()+"");
		card = (TextView) findViewById(R.id.card);
		card.setText(account.getIdcard()+"");
		charger = (TextView) findViewById(R.id.charger);
		charger.setText(account.getJyuser()+"");
		tel = (TextView) findViewById(R.id.tel);
		tel.setText(account.getJyphone()+"");
		phone = (TextView) findViewById(R.id.phone);
		phone.setText(account.getPhone()+"");
		mobile = (TextView) findViewById(R.id.mobile);
		mobile.setText(account.getJymobile()+"");
		credit = (TextView) findViewById(R.id.credit);
		credit.setText(account.getQiyexinyu()+"");
		path = (TextView) findViewById(R.id.path);
		path.setText(account.getQywanlaixianlu()+"");
		
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.user, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}
	
	public class LoadTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject request = new SoapObject(Constant.NAMESPACE, "UserInfo");
			request.addProperty("userNo", SessionManager.getInstance().getUsername());
			request.addProperty("userType", SessionManager.getInstance().getUsertype());
			request.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			
			try {
				transport.call(Constant.NAMESPACE + "UserInfo", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("UserInfoResult"));
					JSONObject json = (JSONObject) parser.nextValue();
//					Log.i("suxoyo", json.toString());
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						JSONObject info = data.getJSONObject("row");
						Log.i("suxoyo", info.toString());
						account = new UserInfo();
						account.setNo(info.getString("no"));
						account.setName(info.getString("name"));
						account.setIdcard(info.getString("idcard"));
						account.setPhone(info.getString("phone"));
						account.setJgcode(info.getString("jgcode"));
						account.setPbussinssname(info.getString("pbussinssname"));
						account.setAddress(info.getString("address"));
						account.setFrname(info.getString("frname"));
						account.setEmail(info.getString("email"));
						account.setFrzjtype(info.getString("frzjtype"));
						account.setJychuanz(info.getString("jychuanz"));
						account.setJymobile(info.getString("jymobile"));
						account.setJyphone(info.getString("jyphone"));
						account.setJyuser(info.getString("jyuser"));
						account.setPasstimu(info.getString("passtimu"));
						account.setPassanswer(info.getString("passanswer"));
						account.setPostcode(info.getString("postcode"));
						account.setQyjingyinfanwei(info.getString("qyjingyinfanwei"));
						account.setQywanlaixianlu(info.getString("qywanlaixianlu"));
						account.setQiyexinyu(info.getString("qiyexinyu"));
						return true;
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
				Log.i("suxoyo", e.toString());
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				init();
			}else {
//				Toast.makeText(UserActivity.this, "No", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}


}
