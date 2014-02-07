package com.honglang.lugang.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import com.honglang.lugang.R.layout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CompanyActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private ListView mListView;
	private List<Company> items;
	private CompanyAdapter adapter;
	private String city;
	private int pageSize;
	private int pageIndex;
	private ProgressDialog progress;
	private String action = "WlyList";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		city = SessionManager.getInstance().getCity();
		pageSize = 40;
		pageIndex = 1;
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.company);
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		mListView = (ListView) this.findViewById(R.id.list_company);
		items = new  ArrayList<Company>();
		new LoadTask().execute((Void)null);

		adapter = new CompanyAdapter(items, this);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(CompanyActivity.this,CompanyDetailActivity.class);
				intent.putExtra("Company", items.get(arg2));
				CompanyActivity.this.startActivity(intent);
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
		
	}

	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(CompanyActivity.this, null, "加载中...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("city", city);
			rpc.addProperty("name", "");
			rpc.addProperty("pageSize", pageSize);
			rpc.addProperty("pageIndex", pageIndex);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + action, envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("WlyListResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						JSONArray rows = data.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							Company item = new Company();
							item.setAddress(obj.getString("address"));
							item.setFk_station(obj.getString("fk_station"));
							item.setName(obj.getString("name"));
							item.setNo(obj.getString("no"));
							item.setPhone(obj.getString("phone"));
							item.setQiyexinyu(obj.getString("qiyexinyu"));
							item.setQywanlaixianlu(obj.getString("qywanlaixianlu"));
							item.setQyjingyinfanwei(obj.getString("qyjingyinfanwei"));
							item.setJychuanz(obj.getString("jychuanz"));
							items.add(item);
						}
						return true;
					} else {
//						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if(result){
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
		
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.company, menu);
//		return true;
//	}

}
