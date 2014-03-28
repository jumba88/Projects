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
import com.honglang.lugang.R.layout;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.office.Bill;
import com.honglang.lugang.office.DealingActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
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

public class FormListActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	
	private ListView mListView;
	private List<HashMap<String, String>> items;
	private FormAdapter adapter;
	
	private int pageSize;
	private int pageIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_list);
		
		title = (TextView) this.findViewById(R.id.title);
		title.setText("暂存跟车单");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		pageSize = 40;
		pageIndex = 1;
		
		items = new ArrayList<HashMap<String,String>>();
		adapter = new FormAdapter(this, items);
		mListView = (ListView) this.findViewById(R.id.formList);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, String> item = items.get(arg2);
				Intent i = new Intent(FormListActivity.this, OutActivity.class);
				i.putExtra("pcId", item.get("id"));
				i.putExtra("from", 1);
				startActivity(i);
			}
		});
		
	}

	@Override
	protected void onResume() {
		new LoadTask().execute((Void)null);
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}
	
	public class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, "GetPCList");
			rpc.addProperty("currentUserNo", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			rpc.addProperty("pageSize", pageSize);
			rpc.addProperty("pageIndex", pageIndex);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + "GetPCList", envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("GetPCListResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					Log.i("suxoyo", json.toString());
					if (json.getBoolean("result")) {
						items.clear();
						JSONObject data = json.getJSONObject("data");
						JSONArray rows = data.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("id", obj.getString("id"));
							item.put("keycode", obj.getString("keycode"));
							item.put("chep", obj.getString("chep"));
							item.put("sij", obj.getString("sij"));
							item.put("sjdh", obj.getString("sjdh"));
							item.put("adddate", obj.getString("adddate"));
							item.put("usid", obj.getString("usid"));
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
				errMsg = e.toString();
				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(FormListActivity.this, errMsg, Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
		
	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.form_list, menu);
//		return true;
//	}

}
