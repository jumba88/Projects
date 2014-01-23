package com.honglang.lugang.home;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.honglang.lugang.Constant;
import com.honglang.lugang.HlApp;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomeActivity extends  FragmentActivity implements OnClickListener{

	private FragmentManager fManager;
	private String handlingAction = "DealingCount";
	private RadioGroup tabs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		new HandlingTask().execute((Void)null);
		HlApp.getInstance().setHomeActivity(HomeActivity.this);
		tabs = (RadioGroup) this.findViewById(R.id.tabs);
		
		tabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Fragment tab = null;
				switch (checkedId) {
				case R.id.one:
					tab = TabOne.newInstance();
					break;
				case R.id.two:
					tab = TabTwo.newInstance();
					break;
				case R.id.three:
					tab = TabThree.newInstance();
					break;
				case R.id.four:
					tab = TabFour.newInstance();
					break;
				}
				fManager = getSupportFragmentManager();
				FragmentTransaction transaction = fManager.beginTransaction();
				transaction.replace(R.id.content, tab).commit();
			}
		});
		((RadioButton)tabs.findViewById(R.id.one)).setChecked(true);
	}
	
	class HandlingTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, handlingAction);
			rpc.addProperty("currentUserNo", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			Log.i("TAG", SessionManager.getInstance().getTokene());
//			rpc.addProperty("pageSize", 20);
//			rpc.addProperty("pageIndex", 1);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			
			try {
				transport.call(Constant.NAMESPACE + handlingAction, envelope);
				SoapObject obj = (SoapObject) envelope.bodyIn;
				if(obj != null){
					Log.i("TAG", obj.getPropertyAsString("DealingCountResult"));
					return true;
				}
			} catch (HttpResponseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			return false;
		}
		
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.home, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	

}
