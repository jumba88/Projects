package com.honglang.lugang.assign;

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
import com.honglang.lugang.assign.AssignActivity.LoadTask;

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

public class SearchStuffDialog extends Dialog implements android.view.View.OnClickListener {

	private EditText to;
	private EditText from;
	private Button search;
	
	private ListView mListView;
	private List<Assign> items;
	private AssignAdapter adapter;
	private int pageSize;
	private int pageIndex;
	private ProgressDialog progress;
	private String action = "PhList";
	
	private Activity activity;
	public SearchStuffDialog(Activity activity, int theme) {
		super(activity, theme);
		this.activity = activity;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_stuff);
		
		to = (EditText) findViewById(R.id.to);
		from = (EditText) findViewById(R.id.from);
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(this);
		
		pageSize = 40;
		pageIndex = 1;
		mListView = (ListView) this.findViewById(R.id.list_assign);
		items = new ArrayList<Assign>();
		adapter = new AssignAdapter(items, activity);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(activity,StuffDetailActivity.class);
				intent.putExtra("Assign", items.get(arg2));
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
			}
			new LoadTask().execute((Void)null);
			break;

		default:
			break;
		}
	}
	
	class LoadTask extends AsyncTask<Void, Void, Integer>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
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
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("PhListResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						JSONArray rows = data.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							Assign item = new Assign();
							item.setAdddate(obj.getString("adddate"));
							item.setDanwei(obj.getString("danwei"));
							item.setDetails(obj.getString("details"));
							item.setEndaddr(obj.getString("endaddr"));
							item.setName(obj.getString("name"));
							item.setPhone(obj.getString("phone"));
							item.setRj(obj.getString("rj"));
							item.setSl(obj.getString("sl"));
							item.setStartaddr(obj.getString("startaddr"));
							item.setUsname(obj.getString("usname"));
							item.setYdqx(obj.getString("ydqx"));
							item.setZldanwei(obj.getString("zldanwei"));
							item.setZzl(obj.getString("zzl"));
							items.add(item);
						}
						return 1;
					} else {
						errMsg = json.getString("msg");
						return 0;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg = e.toString();
				
			} 
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 1) {
				adapter.notifyDataSetChanged();
			}
			
			super.onPostExecute(result);
		}
		
	}


}
