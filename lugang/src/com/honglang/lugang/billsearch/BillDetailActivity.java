package com.honglang.lugang.billsearch;

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
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.truck.Truck;

import android.net.Uri;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BillDetailActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button gdial;
	private Button sdial;
	private TextView name;
	private TextView getter;
	private TextView gphone;
	private TextView end;
	private TextView gaddress;
	private TextView sender;
	private TextView sphone;
	private TextView start;
	private TextView saddress;
	private TextView record;
	private View rec;
	
	private String gnumber;
	private String snumber;
	private String number;
	private ProgressDialog progress;
	private String action = "LZView";
	private List<HashMap<String, String>> stuffList;
	private List<Record> recordList;
	private HashMap<String, String> infoMap;
	private HashMap<String, String> stuffMap;
	private ListView mListView;
	private StuffAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_detail);
		number = this.getIntent().getExtras().getString("number");
		init();
		new SearchTask().execute((Void)null);
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("运单详情");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		name = (TextView) this.findViewById(R.id.name);
		getter = (TextView) this.findViewById(R.id.getter);
		gphone = (TextView) this.findViewById(R.id.gphone);
		end = (TextView) this.findViewById(R.id.end);
		gaddress = (TextView) this.findViewById(R.id.gaddress);
		sender = (TextView) this.findViewById(R.id.sender);
		sphone = (TextView) this.findViewById(R.id.sphone);
		start = (TextView) this.findViewById(R.id.start);
		saddress = (TextView) this.findViewById(R.id.saddress);
		record = (TextView) this.findViewById(R.id.recordView);
		gdial = (Button) this.findViewById(R.id.gdial);
		gdial.setOnClickListener(this);
		sdial = (Button) this.findViewById(R.id.sdial);
		sdial.setOnClickListener(this);
		rec = this.findViewById(R.id.record);
		rec.setOnClickListener(this);
		mListView = (ListView) this.findViewById(R.id.list);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.sdial:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + snumber.replace("-", "")));
			this.startActivity(intent);
			break;
		case R.id.gdial:
			Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + gnumber.replace("-", "")));
			this.startActivity(in);
			break;
		case R.id.record:
			Intent i = new Intent(BillDetailActivity.this, SearchActivity.class);
			i.putExtra("record", recordList.toString());
			this.startActivity(i);
			break;
		}		
	}

	class SearchTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		private String records;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(BillDetailActivity.this, null, "正在查询 ...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("fhCode", number);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + action, envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
//				Log.i("suxoyo", response.toString());
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("LZViewResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						JSONObject form = data.getJSONObject("form");
						JSONObject info = form.getJSONObject("row");
//						Log.i("suxoyo", info.toString());
						infoMap = new HashMap<String, String>();
						infoMap.put("fhcode", info.getString("fhcode"));
						infoMap.put("shr", info.getString("shr"));
						infoMap.put("dizhi", info.getString("dizhi"));
						infoMap.put("dianh", info.getString("dianh"));
						infoMap.put("tocity", info.getString("tocity"));
						infoMap.put("tyxingming", info.getString("tyxingming"));
						infoMap.put("tydianh", info.getString("tydianh"));
						infoMap.put("fromcity", info.getString("fromcity"));
						infoMap.put("tydizhi", info.getString("tydizhi"));
						
						JSONObject list = data.getJSONObject("list");
						JSONArray stuff = list.getJSONArray("rows");
						stuffList = new ArrayList<HashMap<String,String>>();
						for (int i = 0; i < stuff.length(); i++) {
							JSONObject obj = stuff.getJSONObject(i);
							stuffMap = new HashMap<String, String>();
							stuffMap.put("wpmc", obj.getString("wpmc"));
							stuffMap.put("sl_danwei", obj.getString("sl_danwei"));
							stuffMap.put("sl", obj.getString("sl"));
							stuffMap.put("yfsl", obj.getString("yfsl"));
							stuffMap.put("wfsl", obj.getString("wfsl"));
							stuffMap.put("yydsl", obj.getString("yydsl"));
							stuffList.add(stuffMap);
						}
						
						recordList = new ArrayList<Record>();
						JSONObject record = data.getJSONObject("liuzhuan");
						JSONArray rows = record.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							Record recordMap = new Record();
							recordMap.setText(obj.getString("text"));
							recordList.add(recordMap);
							records = records + obj.getString("text");
						}
						return true;
					} else {
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg = e.toString();
//				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				name.setText(infoMap.get("fhcode"));
				getter.setText(infoMap.get("shr"));
				gaddress.setText(infoMap.get("dizhi"));
				gnumber = infoMap.get("dianh");
				gphone.setText(gnumber);
				end.setText(infoMap.get("tocity"));
				sender.setText(infoMap.get("tyxingming"));
				snumber = infoMap.get("tydianh");
				sphone.setText(snumber);
				start.setText(infoMap.get("fromcity"));
				saddress.setText(infoMap.get("tydizhi"));
				record.setText(records);
				
				adapter = new StuffAdapter(stuffList, BillDetailActivity.this);
				if(adapter != null){
					mListView.setAdapter(adapter);
					mListView.setCacheColorHint(0);
					Constant.setListViewHeightBasedOnChildren(mListView);
				}
			}else{
				Toast.makeText(BillDetailActivity.this, errMsg, Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.bill_detail, menu);
//		return true;
//	}

}
