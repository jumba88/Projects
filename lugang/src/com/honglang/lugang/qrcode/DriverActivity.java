package com.honglang.lugang.qrcode;

import java.io.Serializable;
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
import com.honglang.lugang.office.Bill;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DriverActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button confirm;
	private ListView mListView;
	
	private ProgressDialog progress;
	private List<HashMap<String, String>> items;
	private LayoutInflater inflater;
	DriverAdapter adapter;
	
	private Boolean isChecked = false;
	public List<Integer> selected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver);
		
		title = (TextView) this.findViewById(R.id.title);
		title.setText("司机列表");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		confirm = (Button) this.findViewById(R.id.ok);
		confirm.setText("确 认");
		confirm.setVisibility(View.VISIBLE);
		confirm.setOnClickListener(this);
		
		mListView = (ListView) this.findViewById(R.id.driverList);
		
		items = new ArrayList<HashMap<String,String>>();
		inflater = this.getLayoutInflater();
		
		new LoadTask().execute((Void)null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.ok:
			ok();
			break;
		}
		
	}
	
	private void ok(){
		Intent data = new Intent();
		List<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();
//		data.putExtra(name, value)
		for (int i = 0; i < items.size(); i++) {
			if (selected.get(i) == 1) {
				HashMap<String, String> map = items.get(i);
				datas.add(map);
			}
		}
		if (datas.size() == 0) {
			Toast.makeText(this, "请选择一位司机!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (datas.size() > 1) {
			Toast.makeText(this, "只能选择一位司机!", Toast.LENGTH_SHORT).show();
			return;
		}
		data.putExtra("sj", datas.get(0).get("sj"));
		data.putExtra("dh", datas.get(0).get("sjdh"));
		data.putExtra("sfz", datas.get(0).get("sjsfz"));
		this.setResult(RESULT_OK, data);
		this.finish();
	}

	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(DriverActivity.this, null, "正在提交...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "SiJiList");
			rpc.addProperty("currentUserNo", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("pageSize", 40);
			rpc.addProperty("pageIndex", 1);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "SiJiList", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("SiJiListResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					Log.i("suxoyo", json.toString());
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						if (data.getInt("currentrowcount") > 0) {
							JSONArray rows = data.getJSONArray("rows");
							for (int i = 0; i < rows.length(); i++) {
								JSONObject obj = rows.getJSONObject(i);
								HashMap<String, String> item = new HashMap<String, String>();
								item.put("sij", obj.getString("sij"));
								item.put("sjdh", obj.getString("sjdh"));
								item.put("sjsfz", obj.getString("sjsfz"));
								items.add(item);
							}
							return true;
						} else {
							errMsg = "没有司机数据";
							return false;
						}
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
			if (result) {
				adapter = new DriverAdapter();
				if(adapter != null){
					mListView.setAdapter(adapter);
					mListView.setCacheColorHint(0);
//					Constant.setListViewHeightBasedOnChildren(mListView);
				}
			} else {
				Toast.makeText(DriverActivity.this, errMsg, Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
	
	class DriverAdapter extends BaseAdapter{

		@Override
		public int getCount() {
		
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.driver_item, null);
			}
			HashMap<String, String> item = items.get(position);
			if(item == null){
				return null;
			}
			
			final int pos = position;
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView phone = (TextView) convertView.findViewById(R.id.phone);
			TextView id = (TextView) convertView.findViewById(R.id.driverId);
			name.setText(item.get("sij"));
			phone.setText(item.get("sjdh"));
			id.setText(item.get("sjsfz"));
			
			CheckBox check = (CheckBox) convertView.findViewById(R.id.checkBox);
			check.setChecked(isChecked);
			check.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (isChecked) {
						isChecked = false;
						selected.set(pos, 0);
					} else {
						isChecked = true;
						selected.set(pos, 1);
					}
				}
			});
			
			return convertView;
		}
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.driver, menu);
//		return true;
//	}

}
