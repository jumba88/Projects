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
import com.honglang.lugang.R.layout;
import com.honglang.lugang.cityexpress.Express;
import com.honglang.lugang.cityexpress.ExpressActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AssignActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private ListView mListView;
	private List<Assign> items;
	private AssignAdapter adapter;
	private int pageSize;
	private int pageIndex;
	private ProgressDialog progress;
	private String action = "PhList";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assign);
		pageSize = 40;
		pageIndex = 1;
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText(R.string.lastet);
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		
		mListView = (ListView) this.findViewById(R.id.list_assign);
		items = new ArrayList<Assign>();
		new LoadTask().execute((Void)null);
		adapter = new AssignAdapter(items, this);
		if(adapter != null){
			mListView.setAdapter(adapter);
		}
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(AssignActivity.this,StuffDetailActivity.class);
				intent.putExtra("Assign", items.get(arg2));
				AssignActivity.this.startActivity(intent);
				
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

	class LoadTask extends AsyncTask<Void, Void, Integer>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(AssignActivity.this, null, "加载中...");
			progress.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
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
			progress.dismiss();
			if (result == 1) {
				adapter.notifyDataSetChanged();
			}
			
			super.onPostExecute(result);
		}
		
	}

	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.assign, menu);
//		return true;
//	}

}
