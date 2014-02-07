package com.honglang.lugang.truck;

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

public class TruckActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private ListView mListView;
	private List<Truck> items;
	private TruckAdapter adapter;
	private int pageSize;
	private int pageIndex;
	private ProgressDialog progress;
	private String action = "CarList";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_truck);
		pageSize = 40;
		pageIndex = 1;
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.carinfo);
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		mListView = (ListView) this.findViewById(R.id.list_truck);
		items = new ArrayList<Truck>();
		new LoadTask().execute((Void)null);
		adapter = new TruckAdapter(items, this);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(TruckActivity.this,TruckDetailActivity.class);
				intent.putExtra("Truck", items.get(arg2));
				TruckActivity.this.startActivity(intent);
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

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(TruckActivity.this, null, "加载中...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("fromCityName", "");
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
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("CarListResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						JSONObject data = json.getJSONObject("data");
						JSONArray rows = data.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							Truck item = new Truck();
							item.setAdddate(obj.getString("adddate"));
							item.setChep(obj.getString("chep"));
							item.setChex(obj.getString("chex"));
							item.setEndaddr(obj.getString("endaddr"));
							item.setStartaddr(obj.getString("startaddr"));
							item.setPhone(obj.getString("phone"));
							item.setPrice(obj.getString("price"));
							item.setRealname(obj.getString("realname"));
							item.setRongl(obj.getString("rongl"));
							item.setPingp(obj.getString("pingp"));
							item.setRemark(obj.getString("remark"));
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
//		getMenuInflater().inflate(R.menu.truck, menu);
//		return true;
//	}

}
