package com.honglang.lugang.cityexpress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.SessionManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ExpressActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button ok;
	private ListView mListView;
//	private Express item;
	private List<Express> items;
	private ExpressAdapter adapter;
	private String city;
	private int pageSize;
	private int pageIndex;
	private ProgressDialog progress;
	private String action = "TkzxList";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_express);
		city = SessionManager.getInstance().getCity();
		pageSize = 40;
		pageIndex = 1;
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.express);
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		ok = (Button) this.findViewById(R.id.ok);
		ok.setText("查询");
		ok.setVisibility(View.VISIBLE);
		ok.setOnClickListener(this);
		
		mListView = (ListView) this.findViewById(R.id.list_express);
		items = new ArrayList<Express>();
		new LoadTask().execute((Void)null);
		adapter = new ExpressAdapter(items, this);
		
		
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ExpressActivity.this, ExpressDetailActivity.class);
				intent.putExtra("Express", items.get(arg2));
				ExpressActivity.this.startActivity(intent);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.ok:
			SearchExpressDialog dialog = new SearchExpressDialog(this, android.R.style.Theme_Light_NoTitleBar);
			dialog.show();
			break;
		}
		
	}
	
	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(ExpressActivity.this, null, "加载中...");
			progress.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("fromCityName", city);
			rpc.addProperty("toCityName", "");
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
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("TkzxListResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						JSONArray rows = data.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							Express item = new Express();
							item.setId(obj.getString("id"));
							item.setFromcity(obj.getString("fromcity"));
							item.setTocity(obj.getString("tocity"));
							item.setFromaddress(obj.getString("fromaddress"));
							item.setToaddress(obj.getString("toaddress"));
							item.setHaevyprice(obj.getString("haevyprice"));
							item.setLightprice(obj.getString("lightprice"));
							item.setMinprice(obj.getString("minprice"));
							item.setDetails(obj.getString("details"));
							item.setWly_name(obj.getString("wly_name"));
							item.setWly_phone(obj.getString("wly_phone"));
							items.add(item);
						}
						return true;
					} else {
						errMsg = json.getString("msg");
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
			if (result) {
				adapter.notifyDataSetChanged();
			}
			
			super.onPostExecute(result);
		}
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.express, menu);
//		return true;
//	}

}
