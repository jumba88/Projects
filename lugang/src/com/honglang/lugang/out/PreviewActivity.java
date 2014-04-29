package com.honglang.lugang.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.honglang.lugang.company.Company;
import com.honglang.lugang.company.CompanyActivity;
import com.honglang.lugang.login.LoginActivity;
import com.honglang.lugang.office.DealingActivity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class PreviewActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	
	private String keyCode;
	
	private ListView mListView;
	private PreviewAdapter adapter;
	private List<HashMap<String, String>> items;
	
	private boolean isFirst = true;
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);

		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		switch (getIntent().getIntExtra("type", 0)) {
		case 0:
			title.setText("出库详情");
			break;
		case 1:
			title.setText("直接交付出库");
			break;
		case 2:
			title.setText("中转配车出库");
			break;
		}
		keyCode = getIntent().getStringExtra("keycode");
		mListView = (ListView) findViewById(R.id.list);
		items = new ArrayList<HashMap<String,String>>();
		new LoadTask().execute((Void)null);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}

	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			if (isFirst) {
				progress = ProgressDialog.show(PreviewActivity.this, null, "加载中...", false, false);
			}
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "ChuKuDanMxb");
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("keyCode", keyCode);
			Log.i("suxoyo", keyCode);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "ChuKuDanMxb", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("ChuKuDanMxbResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						JSONArray rows = data.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("rkid", obj.getString("rkid"));
							item.put("fhcode", obj.getString("fhcode"));
							item.put("keycode", obj.getString("keycode"));
							item.put("wpmc", obj.getString("wpmc"));
							item.put("zongliang", obj.getString("zongliang"));
							item.put("zongliangdanwei", obj.getString("zongliangdanwei"));
							item.put("tiji", obj.getString("tiji"));
							item.put("sl", obj.getString("sl"));
							item.put("huoh", obj.getString("huoh"));
							item.put("tocity", obj.getString("tocity"));
							items.add(item);
						}
						return true;
					} else {
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg = "加载失败，请稍后再试";
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				adapter = new PreviewAdapter(items, PreviewActivity.this);
				mListView.setAdapter(adapter);
			} else {
				Toast.makeText(PreviewActivity.this, errMsg, Toast.LENGTH_LONG).show();
				if (errMsg.equals("请先登录")) {
					Intent intent = new Intent(PreviewActivity.this, LoginActivity.class);
					intent.putExtra("dir", 1);
					PreviewActivity.this.startActivity(intent);
				}
				
				PreviewActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
	}

}
