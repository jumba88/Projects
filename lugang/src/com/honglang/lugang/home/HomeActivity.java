package com.honglang.lugang.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.honglang.lugang.Constant;
import com.honglang.lugang.HlApp;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.login.UserInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.Toast;

public class HomeActivity extends  FragmentActivity implements OnClickListener{

	public List<Fragment> fragments = new ArrayList<Fragment>();
	FragmentTabAdapter tabAdapter;
	private RadioGroup tabs;
	
	private long exitTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		HlApp.getInstance().setHomeActivity(HomeActivity.this);
		
		fragments.add(new TabOne());
		fragments.add(new TabTwo());
		fragments.add(new TabThree());
		fragments.add(new TabFour());
		
		tabs = (RadioGroup) this.findViewById(R.id.tabs);
		tabAdapter = new FragmentTabAdapter(this, fragments, R.id.content, tabs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                System.out.println("Extra---- " + index + " checked!!! ");
            }
        });
		
        new LoadTask().execute((Void)null);
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.home, menu);
//		return true;
//	}
	
	public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出路港网",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	//load the login user info
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
					Log.i("suxoyo", json.toString());
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						JSONObject info = data.getJSONObject("row");
						UserInfo account = new UserInfo();
						account.setNo(info.getString("no"));
						account.setName(info.getString("name"));
						account.setIdcard(info.getString("idcard"));
						account.setPhone(info.getString("phone"));
//						account.setJgcode(info.getString("jgcode"));
//						account.setPbussinssname(info.getString("pbussinssname"));
//						account.setAddress(info.getString("address"));
//						account.setFrname(info.getString("frname"));
//						account.setEmail(info.getString("email"));
//						account.setFrzjtype(info.getString("frzjtype"));
//						account.setJychuanz(info.getString("jychuanz"));
//						account.setJymobile(info.getString("jymobile"));
//						account.setJyphone(info.getString("jyphone"));
//						account.setJyuser(info.getString("jyuser"));
//						account.setPasstimu(info.getString("passtimu"));
//						account.setPassanswer(info.getString("passanswer"));
//						account.setPostcode(info.getString("postcode"));
//						account.setQyjingyinfanwei(info.getString("qyjingyinfanwei"));
//						account.setQywanlaixianlu(info.getString("qywanlaixianlu"));
//						account.setQiyexinyu(info.getString("qiyexinyu"));
						
						SessionManager.getInstance().setAccount(account);
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			
			super.onPostExecute(result);
		}
		
	}

}
