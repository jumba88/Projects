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

import android.media.AudioManager;
import android.media.SoundPool;
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
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener {

	private TextView title;
	private TextView nothing;
	private Button back;
	private Button ok;
	private String number;
	private ListView mListView;
//	private List<String> items;
	private SearchAdapter adapter;
	private List<String> recordList;
	private ProgressDialog progress;
	private String action = "LZView";
	
	private SoundPool soundPool;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		number = this.getIntent().getExtras().getString("number");
		init();
	}
	
	private void init(){
		nothing = (TextView) this.findViewById(R.id.nothing);
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.record);
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		ok = (Button) this.findViewById(R.id.ok);
		ok.setText("运单详情");
		ok.setVisibility(View.VISIBLE);
		ok.setOnClickListener(this);
		
		soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this,R.raw.success,1);
		soundPool.load(this,R.raw.failed,1);
		
		recordList = new ArrayList<String>();
		mListView = (ListView) this.findViewById(R.id.list_search);
//		items = new ArrayList<String>();
//		items = this.getIntent().getExtras().getStringArrayList("record");
//		Log.i("suxoyo", ""+items.toString());
		new SearchTask().execute((Void)null);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.ok:
			Intent intent = new Intent(SearchActivity.this,BillDetailActivity.class);
			intent.putExtra("code", number);
			startActivity(intent);
			break;
		}
	}

	class SearchTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(SearchActivity.this, null, "正在查询 ...", false, false);
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
//						JSONObject form = data.getJSONObject("form");
//						JSONObject info = form.getJSONObject("row");
//						Log.i("suxoyo", info.toString());
						JSONObject record = data.getJSONObject("liuzhuan");
						JSONArray rows = record.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							recordList.add(obj.getString("text"));
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
				soundPool.play(1, 1, 1, 1, 0, 1);
				
				if (recordList.size() > 0) {
					adapter = new SearchAdapter(recordList, SearchActivity.this);
					if (adapter != null) {
						mListView.setAdapter(adapter);
					}
				} else {
					nothing.setVisibility(View.VISIBLE);
				}
				
			}else{
				soundPool.play(2, 1, 1, 1, 0, 1);
				nothing.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.search, menu);
//		return true;
//	}

}
