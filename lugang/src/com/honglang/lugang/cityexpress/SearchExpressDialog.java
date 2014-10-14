package com.honglang.lugang.cityexpress;

import java.util.ArrayList;
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
import com.honglang.lugang.cityexpress.ExpressActivity.LoadTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchExpressDialog extends Dialog implements android.view.View.OnClickListener {

	private EditText to;
	private EditText from;
	private Button search;
	
	private TextView zero;
	
	private ListView mListView;
	private List<Express> items;
	private ExpressAdapter adapter;
	private int pageSize;
	private int pageIndex;
	private String action = "TkzxList";
	
	private Activity activity;
	public SearchExpressDialog(Activity activity, int theme) {
		super(activity, theme);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_express);
		
		to = (EditText) findViewById(R.id.to);
		from = (EditText) findViewById(R.id.from);
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(this);
		
		zero = (TextView) findViewById(R.id.zero);
		
		pageSize = 40;
		pageIndex = 1;
		mListView = (ListView) findViewById(R.id.list_express);
		items = new ArrayList<Express>();
		adapter = new ExpressAdapter(items, activity);
		
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(activity, ExpressDetailActivity.class);
				intent.putExtra("Express", items.get(arg2));
				activity.startActivity(intent);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			search.setEnabled(false);
			if (items.size() > 0) {
				items.clear();
				adapter.notifyDataSetChanged();
			}else {
				zero.setVisibility(View.GONE);
			}
			new LoadTask().execute((Void)null);
			break;

		default:
			break;
		}
	}
	
	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("fromCityName", from.getText()+"");
			rpc.addProperty("toCityName", to.getText()+"");
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
			if (result) {
				if (items.size() == 0) {
					zero.setVisibility(View.VISIBLE);
				}else {
					adapter.notifyDataSetChanged();
				}
			}
			search.setEnabled(true);
			super.onPostExecute(result);
		}
		
	}


}
