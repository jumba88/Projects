package com.honglang.lugang.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

public class HomeActivity extends  FragmentActivity implements OnClickListener{

	private FragmentManager fManager;
	private String handlingAction = "DealingCount";
	public List<Fragment> fragments = new ArrayList<Fragment>();
	FragmentTabAdapter tabAdapter;
	private RadioGroup tabs;
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
		
//		new HandlingTask().execute((Void)null);
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
