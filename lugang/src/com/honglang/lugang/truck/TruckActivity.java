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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.honglang.lugang.Constant;
import com.honglang.lugang.R;
import com.honglang.lugang.R.layout;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TruckActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private Button ok;
	
	private PullToRefreshListView mListView;
	private List<Truck> items;
	private TruckAdapter adapter;
	private int pageSize;
	private int pageIndex;
	private ProgressDialog progress;
	private String action = "CarList";
	private int totalCount;
	private boolean ISFIRST = true;
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
		ok = (Button) this.findViewById(R.id.ok);
		ok.setText("查询");
		ok.setVisibility(View.VISIBLE);
		ok.setOnClickListener(this);
		
		mListView = (PullToRefreshListView) this.findViewById(R.id.list_truck);
		mListView.setMode(Mode.PULL_FROM_END);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				new LoadTask().execute((Void)null);
			}
		});
		
		items = new ArrayList<Truck>();
		new LoadTask().execute((Void)null);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(TruckActivity.this,TruckDetailActivity.class);
				intent.putExtra("Truck", items.get(arg2-1));
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
		case R.id.ok:
			SearchTruckDialog dialog = new SearchTruckDialog(this, android.R.style.Theme_Light_NoTitleBar);
			dialog.show();
			break;
		}
		
	}

	class LoadTask extends AsyncTask<Void, Void, Boolean>{

		private String errMsg;
		@Override
		protected void onPreExecute() {
			if (ISFIRST) {
				progress = ProgressDialog.show(TruckActivity.this, null, "加载中...", false, false);
			}
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
						totalCount = data.getInt("totalrowcount");
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
				if (ISFIRST) {
					adapter = new TruckAdapter(items, TruckActivity.this);
					if(adapter != null){
						mListView.setAdapter(adapter);
					}
				}else{
					adapter.notifyDataSetChanged();
				}
				ISFIRST = false;
				
				if (pageIndex > 1) {
					mListView.onRefreshComplete();
				}
				if (totalCount == items.size()) {
					mListView.setMode(Mode.DISABLED);
					Toast.makeText(TruckActivity.this, "已加载完所有数据", Toast.LENGTH_SHORT).show();
				}
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
