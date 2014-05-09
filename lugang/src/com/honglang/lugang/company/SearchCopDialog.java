package com.honglang.lugang.company;

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
import com.honglang.lugang.company.CompanyActivity.LoadTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchCopDialog extends Dialog implements android.view.View.OnClickListener {

	private EditText city;
	private EditText name;
	private Button search;
	
	private TextView zero;
	
	private ListView mListView;
	private List<Company> items;
	private CompanyAdapter adapter;
	private int pageSize;
	private int pageIndex;
	private ProgressDialog progress;
	private String action = "WlyList";
	
	private Activity activity;
	public SearchCopDialog(Activity activity, int theme) {
		super(activity, theme);
		this.activity = activity;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_company);
		
		city = (EditText) findViewById(R.id.city);
		name = (EditText) findViewById(R.id.name);
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(this);
		
		zero = (TextView) findViewById(R.id.zero);
		
		pageSize = 40;
		pageIndex = 1;
		
		mListView = (ListView) this.findViewById(R.id.list_company);
		items = new  ArrayList<Company>();

		adapter = new CompanyAdapter(items, activity);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(activity,CompanyDetailActivity.class);
				intent.putExtra("Company", items.get(arg2));
				activity.startActivity(intent);
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
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

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("city", city.getText()+"");
			rpc.addProperty("name", name.getText()+"");
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
			if(result){
				if (items.size() == 0) {
					zero.setVisibility(View.VISIBLE);
				}else {
					adapter.notifyDataSetChanged();
				}
			}
			super.onPostExecute(result);
		}
		
	}

}
