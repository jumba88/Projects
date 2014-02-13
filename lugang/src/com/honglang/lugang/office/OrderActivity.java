package com.honglang.lugang.office;

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
import com.honglang.lugang.CountActivity;
import com.honglang.lugang.R;
import com.honglang.lugang.SessionManager;
import com.honglang.lugang.R.layout;
import com.honglang.lugang.R.menu;
import com.honglang.lugang.billsearch.BillDetailActivity;

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

public class OrderActivity extends Activity implements OnClickListener {

	private TextView title;
	private Button back;
	private TextView stuffCode;
	private TextView end;
	private TextView transport;
	private ListView mListView;
	private List<Order> items;
	private OrderAdapter adapter;
	private ProgressDialog progress;
	private String action = "GetFormInfoByFormOid";
	private Bill bill;
	private HashMap<String, String> infoMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		init();
	}

	private void init(){
		title = (TextView) this.findViewById(R.id.title);
		title.setText("等待货物入库");
		back = (Button) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		bill = (Bill) this.getIntent().getExtras().getSerializable("bill");
		
		stuffCode = (TextView) this.findViewById(R.id.stuffCode);
		end = (TextView) this.findViewById(R.id.end);
		transport = (TextView) this.findViewById(R.id.transport);
		
		mListView = (ListView) this.findViewById(R.id.stuffList);
		items = new ArrayList<Order>();
		new LoadTask().execute((Void)null);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Order order = items.get(arg2);
				Intent intent = new Intent(OrderActivity.this, CountActivity.class);
				intent.putExtra("order", order);
				OrderActivity.this.startActivity(intent);
			}
		});
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.order, menu);
//		return true;
//	}

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
			progress = ProgressDialog.show(OrderActivity.this, null, "加载中...", false, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			SoapObject rpc = new SoapObject(Constant.NAMESPACE, action);
			rpc.addProperty("formOid", Long.parseLong(bill.getForm_oid()));
			rpc.addProperty("nodeid", Long.parseLong(bill.getCurrent_node_id()));
			rpc.addProperty("currentUserno", SessionManager.getInstance().getUsername());
			rpc.addProperty("token", SessionManager.getInstance().getTokene());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(Constant.SERVICE_URL);
			transport.debug = true;
			try {
				transport.call(Constant.NAMESPACE + action, envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				if(response != null){
					JSONTokener parser = new JSONTokener(response.getPropertyAsString("GetFormInfoByFormOidResult"));
					JSONObject json = (JSONObject) parser.nextValue();
					if (json.getBoolean("result")) {
						items.clear();
						JSONObject data = json.getJSONObject("data");
						JSONObject form = data.getJSONObject("form");
						JSONObject info = form.getJSONObject("row");
						infoMap = new HashMap<String, String>();
						infoMap.put("fhcode", info.getString("fhcode"));
						infoMap.put("tocity", info.getString("tocity"));
						infoMap.put("wuliu", info.getString("wuliu"));
						
						JSONObject mxb = data.getJSONObject("mxb");
						JSONArray rows = mxb.getJSONArray("rows");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject obj = rows.getJSONObject(i);
							Log.i("suxoyo", obj.toString());
							Order item = new Order();
//							item.setOID(obj.getString("OID"));
							item.setWplx(obj.getString("wplx"));
							item.setWpmc(obj.getString("wpmc"));
							item.setSl(obj.getString("sl"));
							item.setSl_danwei(obj.getString("sl_danwei"));
							item.setZongliang(obj.getString("zongliang"));
							item.setTiji(obj.getString("tiji"));
							item.setTiji_danwei(obj.getString("tiji_danwei"));
							item.setYunfei(obj.getString("yunfei"));
							item.setTihuofei(obj.getString("tihuofei"));
							item.setSonghuofei(obj.getString("songhuofei"));
							item.setBaof(obj.getString("baof"));
							item.setZongyunfei(obj.getString("zongyunfei"));
							item.setTbjz(obj.getString("tbjz"));
							item.setIsdsf(obj.getString("isdsf"));
							item.setHuok(obj.getString("huok"));
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
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progress.dismiss();
			if (result) {
				stuffCode.setText(infoMap.get("fhcode"));
				end.setText(infoMap.get("tocity"));
				transport.setText(infoMap.get("wuliu"));
				
				adapter = new OrderAdapter(items, OrderActivity.this);
				if(adapter != null){
					mListView.setAdapter(adapter);
					mListView.setCacheColorHint(0);
					Constant.setListViewHeightBasedOnChildren(mListView);
				}
			} else {
				Toast.makeText(OrderActivity.this, errMsg, Toast.LENGTH_SHORT).show();
				OrderActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
	}

}
