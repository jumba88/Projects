package com.honglang.lugang.qrcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.office.Order;
import com.honglang.lugang.office.OrderActivity;
import com.honglang.lugang.office.OrderAdapter;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button confirm;
	
	private ListView mListView;
	private InAdapter adapter;
	
	private List<Form> formList;
	
	private ProgressDialog progress;
	private String fhCode;
	private List<String> ids;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in);
		
		init();
		fhCode = this.getIntent().getExtras().getString("fhCode");
		new LoadTask().execute((Void)null);
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("入库订单");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		confirm = (Button) this.findViewById(R.id.ok);
		confirm.setText("确 认");
		confirm.setVisibility(View.VISIBLE);
		confirm.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.list);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					Form form = formList.get(arg2);
					Intent intent = new Intent(InActivity.this, FormActivity.class);
					intent.putExtra("form", form);
					InActivity.this.startActivity(intent);
			}
		});
		formList = new ArrayList<Form>();
		ids = new ArrayList<String>(); 
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.ok:
			ids.clear(); 
			for (int i = 0; i < formList.size(); i++) {
				Log.i("suxoyo", ""+adapter.selected.get(i));
				if (adapter.selected.get(i) == 1) {
					ids.add(formList.get(i).getInfo().getFormoid());
				}
			}
			if (ids.size() == 0) {
				Toast.makeText(this, "请选择订单!", Toast.LENGTH_SHORT).show();
				return;
			} else {
				new ConfirmTask().execute((Void)null);
			}
			break;
		}
		
	}

	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
//			progress = ProgressDialog.show(InActivity.this, null, "加载中...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "GetFormInfoByFhcode");
			rpc.addProperty("fhCode", fhCode);
			rpc.addProperty("currUserNo", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "GetFormInfoByFhcode", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("GetFormInfoByFhcodeResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					Log.i("suxoyo", json.toString());
					JSONObject data = json.getJSONObject("data");
					if (json.getBoolean("result")) {
						if (data.getInt("currentrowcount") > 0) {
							JSONArray rows = data.getJSONArray("rows");
							
							for (int i = 0; i < rows.length(); i++) {
								Form f = new Form();
								
								JSONObject forms = rows.getJSONObject(i);
								JSONObject form = forms.getJSONObject("form");
								JSONObject row = form.getJSONObject("row");
								Info info = new Info();
								info.setFhcode(row.getString("fhcode"));
								info.setFormoid(row.getString("formoid"));
								info.setFhrxingming(row.getString("fhrxingming"));
								info.setFhrsfz(row.getString("fhrsfz"));
								info.setFhrdianh(row.getString("fhrdianh"));
								info.setFhrdizhi(row.getString("fhrdizhi"));
								info.setFromcity(row.getString("fromcity"));
								info.setTocity(row.getString("tocity"));
								info.setTyxingming(row.getString("tyxingming"));
								info.setTysfz(row.getString("tysfz"));
								info.setTydianh(row.getString("tydianh"));
								info.setTydizhi(row.getString("tydizhi"));
								
								JSONObject mxb = forms.getJSONObject("mxb");
								JSONArray stuff = mxb.getJSONArray("rows");
								List<HashMap<String, String>> stuffList = new ArrayList<HashMap<String,String>>();
								for (int j = 0; j < stuff.length(); j++) {
									JSONObject obj = stuff.getJSONObject(j);
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("wplx", obj.getString("wplx"));
									map.put("wpmc", obj.getString("wpmc"));
									map.put("sl", obj.getString("sl"));
									map.put("yunf", obj.getString("yunf"));
									map.put("sl_danwei", obj.getString("sl_danwei"));
									map.put("chep", obj.getString("chep"));
									stuffList.add(map);
								}
								
								f.setInfo(info);
								f.setStuff(stuffList);
								formList.add(f);
							}
							return true;
						}else {
							errMsg = "没有对应订单";
							return false;
						}
						
					} else {
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg = "加载订单失败,请检查您的网络是否正常";
//				errMsg = e.toString();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
//			progress.dismiss();
			if (result) {
				adapter = new InAdapter(InActivity.this, formList);
				if(adapter != null){
					mListView.setAdapter(adapter);
					mListView.setCacheColorHint(0);
//					Constant.setListViewHeightBasedOnChildren(mListView);
				}
				if (formList.size() == 1) {
					ids.add(formList.get(0).getInfo().getFormoid());
					new ConfirmTask().execute((Void)null);
				}
			} else {
				Toast.makeText(InActivity.this, errMsg, Toast.LENGTH_SHORT).show();
//				InActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
	}

	class ConfirmTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(InActivity.this, null, "正在提交...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "ZhongZhuanRuKuByArrFormIds");
			rpc.addProperty("jsonFormOidArr", ids.toString());
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "ZhongZhuanRuKuByArrFormIds", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
//				errMsg = response.toString();
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("ZhongZhuanRuKuByArrFormIdsResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
//						errMsg = json.getString(name);
						return true;
					}else{
						errMsg = json.getString("msg");
						return false;
					}
				}
			} catch (Exception e) {
//				errMsg = e.toString();
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				Toast.makeText(InActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
				if (formList.size() == 1) {
					formList.clear();
				} else {
					for (int i = 0; i < formList.size(); i++) {
						if (adapter.selected.get(i) == 1) {
							formList.remove(i);
						}
					}
				}
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(InActivity.this, "操作失败,"+errMsg, Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.in, menu);
//		return true;
//	}

}
