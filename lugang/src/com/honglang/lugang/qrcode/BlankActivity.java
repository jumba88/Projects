package com.honglang.lugang.qrcode;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.StuffActivity;
import com.honglang.lugang.office.CountActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BlankActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button confirm;
	private String fhCode;
	
	private ProgressDialog progress;
	
	private Button add;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blank);
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("空白托运单");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		confirm = (Button) this.findViewById(R.id.ok);
		confirm.setText("确 认");
		confirm.setVisibility(View.VISIBLE);
		confirm.setOnClickListener(this);
		fhCode = this.getIntent().getStringExtra("fhCode");
		
		new LoadTask().execute((Void)null);
		
		add = (Button) this.findViewById(R.id.add);
		add.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			Intent intent = new Intent(this, StuffActivity.class);
			this.startActivityForResult(intent, 111);
			break;
		case R.id.ok:
//			ok();
			break;
		}
	}

	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(BlankActivity.this, null, "正在验证发货码...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "YanZhengTuoYunDan");
			rpc.addProperty("currentUserNo", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("fhCode", fhCode);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "YanZhengTuoYunDan", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("YanZhengTuoYunDanResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					Log.i("suxoyo", json.toString());
					if (json.getBoolean("result")) {
						return true;
					} else {
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg = e.toString();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (!result) {
				Toast.makeText(BlankActivity.this, errMsg, Toast.LENGTH_SHORT).show();
				BlankActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
	}
	
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.blank, menu);
//		return true;
//	}

}
